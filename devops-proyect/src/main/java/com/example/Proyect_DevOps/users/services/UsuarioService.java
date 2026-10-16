package com.example.Proyect_DevOps.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.users.models.RolModel;
import com.example.Proyect_DevOps.users.models.UsuarioModel;
import com.example.Proyect_DevOps.users.repositories.RolRepository;
import com.example.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.example.Proyect_DevOps.utilities.AESUtil;
import com.example.Proyect_DevOps.utilities.HMACUtil;
import com.example.Proyect_DevOps.utilities.JWTUtil;

@Service
public class UsuarioService extends GenericServiceImpl<UsuarioModel, Integer, UsuarioRepository> {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioModel> findAll() {
        return mostrarUsuarios();
    }

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

    @Override
    public UsuarioModel save(UsuarioModel entity) {
        return guardaUsuario(entity);
    }

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

    @Override
    public boolean deleteById(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    /*
    ** Este metodo recibe los datos de correo y contraseña del login
    ** si el usuario que se ingreso el correo no esta dentro del sistema
    ** retorna false, si el usuario existe encripta la contraseña y la busca en el usuario
    ** retorna true si la contraseña es correcta y si no retorna false
    ** @param correo primer dato del usuario 
    ** @param contrasena segundo dato del usuario
    ** @return true si las credenciales son correctas o return false si no lo son
    */
    public UsuarioModel validacionDeLogin(String correo, String contrasena){
        Optional<UsuarioModel> usuarioOpt = repository.findByCorreoHMAC(HMACUtil.GenerarHuella(correo));
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            if (passwordEncoder.matches(contrasena, usuario.getContraseña())) {
                return usuario;
            } else {
                throw new IllegalArgumentException("Correo o Contraseña Incorrectas");
            }
        } else {
            throw new IllegalArgumentException("No se pudo obtener el nombre del usuario");
        }
    }

    /*
        Con el correo del que inicia secion se extrae del modelo el objeto rol y 
        de este se extrae el id que contenga este mismo si no se encontro el usuario por correo
        retorna un -1
        @param correo dato del usuario
        @return el idRol del usuario si este fue encontrado en la base de datos
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

    public List<UsuarioModel> mostrarUsuarios(){
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
    
    public UsuarioModel guardaUsuario(UsuarioModel usuario){
        if (repository.existsByCorreoHMAC(HMACUtil.GenerarHuella(usuario.getCorreo()))){
            throw new RuntimeException("El correo que se registro ya existe");
        } 
        if (usuario.getRol() == null || usuario.getRol().getIdRol() <= 0){
            throw new RuntimeException("El rol es obligatorio");
        }
        Optional<RolModel> rolOpt = rolRepository.findById(usuario.getRol().getIdRol());
        if (rolOpt.isPresent()) {
            RolModel rol = rolOpt.get();
            usuario.setRol(rol);
        } else {
            throw new RuntimeException("El rol que ingreso no se encontro en la base de datos");
        }
        
        usuario.setCorreoHMAC(HMACUtil.GenerarHuella(usuario.getCorreo()));
        usuario.setCorreo(AESUtil.encriptar(usuario.getCorreo()));
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        return repository.save(usuario);
    }

    /*
        Extrae el nombre del Usuario que ingreso su correo dentro del frontend
        si no lo encuentra retorna un "User not found" y si lo encuentra 
        retorna el nombre completo del usuario 
        @param correo es el dato que se requiere para buscar el usuario
        @return el nombre del usuario encontrado, si no se encuentra regresa un "User not found"
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

    public UsuarioModel fingById(int Id) {
        Optional<UsuarioModel> entity = repository.findById(Id);
        UsuarioModel entidad = entity.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        try {
            entidad.setCorreo(AESUtil.desencriptar(entidad.getCorreo()));
        } catch (Exception e) {
            // ignore
        }
        return entidad;
    }

    public void removerUsuario(int id) {
        repository.deleteById(id);
    }
}