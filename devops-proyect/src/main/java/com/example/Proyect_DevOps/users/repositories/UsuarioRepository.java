package com.example.Proyect_DevOps.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.users.models.UsuarioModel;


@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer>{
    
    Optional<UsuarioModel> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
