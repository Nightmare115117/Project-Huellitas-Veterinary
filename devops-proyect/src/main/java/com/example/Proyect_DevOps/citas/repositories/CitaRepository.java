package com.example.Proyect_DevOps.citas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.citas.models.CitaModel;
import com.example.Proyect_DevOps.mascotas.models.MascotaModel;
import com.example.Proyect_DevOps.users.models.UsuarioModel;


@Repository
public interface CitaRepository extends JpaRepository<CitaModel, Integer>{

    List<CitaModel> findByUsuarioMascotaAndMascotaModel (UsuarioModel usuarioMascota, MascotaModel mascotaModel);
    long countByUsuarioMascotaAndEstadoCita (UsuarioModel usuario, int estadoCita);
    List<CitaModel> findByUsuarioMascota (UsuarioModel usuario);
}