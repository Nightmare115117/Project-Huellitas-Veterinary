package com.example.Proyect_DevOps.mascotas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.mascotas.models.MascotaModel;
import com.example.Proyect_DevOps.users.models.UsuarioModel;


@Repository
public interface MascotaRepository extends JpaRepository<MascotaModel, Integer>{

    List<MascotaModel> findByUsuario(UsuarioModel usuario);
    long countByUsuario(UsuarioModel usuario);

}
