package com.Proyect_DevOps.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Proyect_DevOps.MFA.MFAService;
import com.Proyect_DevOps.common.services.GenericServiceImpl;
import com.Proyect_DevOps.users.models.RolModel;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.repositories.RolRepository;
import com.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.Proyect_DevOps.utilities.AESUtil;
import com.Proyect_DevOps.utilities.HMACUtil;

/**
 * Servicio de negocio para la gestión integral de usuarios.
 * Administra el registro con cifrado AES-256 en reposo, indexación con HMAC-SHA256,
 * hash seguro de contraseñas con BCrypt, y validación de autenticación de login.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class UsuarioService extends GenericServiceImpl<UsuarioModel, Integer, UsuarioRepository> {

    /**
     * Repositorio de roles para vincular el rol a los usuarios.
     */
    @Autowired
    private RolRepository rolRepository;

    /**
     * Codificador seguro de contraseñas BCrypt.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MFAService service;

    /**
     * {@inheritDoc}
     * Recupera todos los usuarios de la base de datos y descifra sus correos en memoria.
     */
    @Override
    public List<UsuarioModel> findAll() {
        List<UsuarioModel> usuarios = new ArrayList<>();
        for (UsuarioModel entidad : repository.findAll()) {
            try {
                entidad.setCorreo(AESUtil.desencriptar(entidad.getCorreo()));
            } catch (Exception e) {
                // ignore
            }
            usuarios.add(entidad);
        }
        return usuarios;
    }

    /**
     * {@inheritDoc}
     * Busca un usuario por su identificador numérico y descifra su correo electrónico.
     */
    @Override
    public Optional<UsuarioModel> findById(Integer id) {
        return repository.findById(id).map(entidad -> {
            try {
                entidad.setCorreo(AESUtil.desencriptar(entidad.getCorreo()));
            } catch (Exception e) {
                // Mantener correo si no se puede desencriptar
            }
            return entidad;
        });
    }

    /**
     * {@inheritDoc}
     * Valida que el correo no esté previamente registrado, asocia el rol correspondiente,
     * calcula la huella HMAC, cifra el correo con AES y codifica la contraseña con BCrypt antes de persistir.
     *
     * @throws RuntimeException Si el correo ya existe, el rol es inválido o no existe en la base de datos.
     */
    @Override
    public UsuarioModel save(UsuarioModel entity) {
        if (repository.existsByCorreoHMAC(HMACUtil.GenerarHuella(entity.getCorreo()))) {
            throw new RuntimeException("El correo que se registro ya existe");
        }
        if (entity.getRol() == null || entity.getRol().getIdRol() <= 0) {
            throw new RuntimeException("El rol es obligatorio");
        }
        Optional<RolModel> rolOpt = rolRepository.findById(entity.getRol().getIdRol());
        if (rolOpt.isPresent()) {
            RolModel rol = rolOpt.get();
            entity.setRol(rol);
        } else {
            throw new RuntimeException("El rol que ingreso no se encontro en la base de datos");
        }

        entity.setCorreoHMAC(HMACUtil.GenerarHuella(entity.getCorreo()));
        entity.setCorreo(AESUtil.encriptar(entity.getCorreo()));
        entity.setContraseña(passwordEncoder.encode(entity.getContraseña()));
        return repository.save(entity);
    }

    /**
     * {@inheritDoc}
     * Actualiza la información del usuario, recalculando cifrado y hashes si fueron modificados.
     */
    @Override
    public Optional<UsuarioModel> update(Integer id, UsuarioModel entity) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        entity.setIdUsuario(id);

        if (entity.getCorreo() != null && !entity.getCorreo().isBlank()) {
            entity.setCorreoHMAC(HMACUtil.GenerarHuella(entity.getCorreo()));
            entity.setCorreo(AESUtil.encriptar(entity.getCorreo()));
        }

        if (entity.getContraseña() != null && !entity.getContraseña().isBlank()) {
            if (!entity.getContraseña().startsWith("$2a$")) {
                entity.setContraseña(passwordEncoder.encode(entity.getContraseña()));
            }
        }

        if (entity.getRol() != null && entity.getRol().getIdRol() > 0) {
            rolRepository.findById(entity.getRol().getIdRol()).ifPresent(entity::setRol);
        }

        UsuarioModel guardado = repository.save(entity);
        try {
            guardado.setCorreo(AESUtil.desencriptar(guardado.getCorreo()));
        } catch (Exception e) {
            // ignore
        }
        return Optional.of(guardado);
    }

    /**
     * {@inheritDoc}
     * Elimina el usuario identificado por su ID.
     */
    @Override
    public boolean deleteById(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    /**
     * Valida las credenciales proporcionadas para el inicio de sesión.
     * Busca al usuario por la huella HMAC del correo y compara la contraseña con el hash BCrypt almacenado.
     *
     * @param correo Correo electrónico introducido por el usuario.
     * @param contrasena Contraseña en texto plano proporcionada en la solicitud.
     * @return El objeto {@link UsuarioModel} validado si las credenciales son correctas.
     * @throws IllegalArgumentException Si el usuario no existe o las credenciales no coinciden.
     */
    public UsuarioModel validacionDeLogin(String correo, String contrasena, int mfacode){
        Optional<UsuarioModel> usuarioOpt = repository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            if ((contrasena.length() >= 8 && contrasena.length() <= 16) && passwordEncoder.matches(contrasena, usuario.getContraseña())) {
                if (usuario.isMFANeeded() && mfacode != 0) {
                    if (usuario.getSecretoMFA() == null) {
                        throw new IllegalArgumentException("No se pudo verificar su MFA contacte con el administrador");
                    }
                    if (service.verificarCodigo(AESUtil.desencriptar(usuario.getSecretoMFA()), mfacode)) {
                        return usuario;
                    } else {
                        throw new IllegalArgumentException("Codigo incorrecto");
                    }
                } else {
                    return usuario;
                }
            } else {
                throw new IllegalArgumentException("Correo o Contraseña Incorrectas");
            }
        } else {
            throw new IllegalArgumentException("Correo o Contraseñas Incorrectas");
        }
    }

    /**
     * Obtiene el identificador numérico del rol del usuario a partir de su correo electrónico.
     *
     * @param correo Correo del usuario a buscar.
     * @return Identificador del rol asociado al usuario, o -1 si el usuario no fue localizado.
     */
    public Integer buscarIdRol (String correo){
        Optional<UsuarioModel> usuarioOpt = repository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            return usuario.getRol() != null ? usuario.getRol().getIdRol() : -1;
        } else {
            return -1;
        }
    }

    /**
     * Obtiene el nombre completo del usuario a partir de su correo electrónico.
     *
     * @param correo Correo del usuario a buscar.
     * @return Nombre y apellidos concatenados, o {@code "User not found"} si no existe.
     */
    public String extraerNombre(String correo){
        Optional<UsuarioModel> usuarioOpt = repository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            return usuario.getNombre() + " " + (usuario.getPaterno() != null ? usuario.getPaterno() : "") + " " + (usuario.getMaterno() != null ? usuario.getMaterno() : "");
        } else {
            return "User not found";
        }
    }

    /**
     * Busca un usuario por su correo electrónico usando la huella HMAC.
     * Descifra el correo en memoria antes de retornar.
     *
     * @param correo Correo electrónico en texto plano.
     * @return El {@link UsuarioModel} encontrado, o {@code null} si no existe.
     */
    public UsuarioModel buscarPorCorreo(String correo) {
        Optional<UsuarioModel> usuarioOpt = repository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuario = usuarioOpt.get();
            try {
                usuario.setCorreo(AESUtil.desencriptar(usuario.getCorreo()));
            } catch (Exception e) {
                // Mantener correo si no se puede desencriptar
            }
            return usuario;
        }
        return null;
    }

    /**
     * Actualiza exclusivamente los campos relacionados con MFA del usuario
     * (secretoMFA, secretoPendienteMFA, mfaneed) sin alterar el cifrado del correo ni la contraseña.
     *
     * @param usuario Entidad con los campos MFA actualizados.
     */
    public void actualizarMFA(UsuarioModel usuario) {
        repository.save(usuario);
    }
}
