package com.example.Proyect_DevOps.consultaMedica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.consultaMedica.models.TratamientoModel;

@Repository
public interface TratamientoRepository extends JpaRepository<TratamientoModel, Integer>{

    List<TratamientoModel> findByconsultas_cita_usuarioMascota_correo(String correo);

    List<TratamientoModel> findByconsultas_cita_usuarioMascota_correoAndConsultas_cita_mascotaModel_idMascota(String correo, int idMascota);

    long countByconsultas_cita_usuarioMascota_correoAndEstatus(String correo, int estatus);
}
