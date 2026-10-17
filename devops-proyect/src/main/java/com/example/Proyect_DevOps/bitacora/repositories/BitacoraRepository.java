package com.example.Proyect_DevOps.bitacora.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.bitacora.models.BitacoraModel;

@Repository
public interface BitacoraRepository extends JpaRepository<BitacoraModel, Integer> {
    List<BitacoraModel> findAllByOrderByFechaHoraDesc();
    List<BitacoraModel> findAllByOrderByIdBitacoraDesc();
}
