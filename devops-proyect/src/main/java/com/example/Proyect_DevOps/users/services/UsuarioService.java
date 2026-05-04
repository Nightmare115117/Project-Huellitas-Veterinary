package com.example.Proyect_DevOps.users.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.users.models.RolModel;
import com.example.Proyect_DevOps.users.models.UsuarioModel;
import com.example.Proyect_DevOps.users.repositories.RolRepository;
import com.example.Proyect_DevOps.users.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
    ** Este metodo recibe los datos de correo y contraseña del login
    ** si el usuario que se ingreso el correo no esta dentro del sistema
    ** retorna false, si el usuario existe encripta la contraseña y la busca en el usuario
    ** retorna true si la contraseña es correcta y si no retorna false
    ** @param correo primer dato del usuario 
    ** @param contrasena segundo dato del usuario
    ** @return true si las credenciales son correctas o return false si no lo son
    */
    public boolean validacionDeLogin(String correo, String contrasena){
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            //return usuario.getContraseña().equalsIgnoreCase(contrasena);
            return passwordEncoder.matches(contrasena, usuario.getContraseña());
        } else {
            return false;
        }
    }


    /*
        Con el correo del que inicia secion se extrae del modelo el objeto rol y 
        de este se extrae el id que contenga este mismo si no se encontro el el usuario por correo
        retorna un -1
        @param correo dato del usuario
        @return el idRol del usuario si este fue encontrado en la base de datos
    */
    public Integer buscarIdRol (String correo){
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            System.out.println(usuario.getRol().getIdRol());
            return usuario.getRol().getIdRol();
        } else {
            return -1;
        }
    }

    public List<UsuarioModel> mostrarUsuarios(){
        return usuarioRepository.findAll();
    }
    
    public UsuarioModel guardaUsuario(UsuarioModel usuario){
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())){
            throw new RuntimeException("El correo que se registro ya existe");
        } 
        System.out.println(usuario.getRol());
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
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        return usuarioRepository.save(usuario);
    }

    /*
        Extrae el nombre del Usuario que ingreso su correo dentro del forntend
        si no lo encuentra retorna un "User not found" y si lo encuentra 
        retorna el nombre completo del usuario 
        @param correo es el dato que se requiere para buscar el usuario
        @return el nombre del usuario encontrado, si no se encuentra regresa un "User not found"
    */
    public String extraerNombre(String correo){
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()){
            UsuarioModel usuario = usuarioOpt.get();
            return usuario.getNombre() + " " + usuario.getPaterno()+ " " + usuario.getMaterno();
        } else {
            return "User not found";
        }
    }
}
