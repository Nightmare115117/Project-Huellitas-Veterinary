package com.example.Proyect_DevOps.users.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.users.models.RolModel;
import com.example.Proyect_DevOps.users.repositories.RolRepository;

/**
 * Servicio de negocio para la administración de roles del sistema.
 * Extiende las operaciones CRUD genéricas provistas por {@link GenericServiceImpl}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class RolService extends GenericServiceImpl<RolModel, Integer, RolRepository> {

    /**
     * Inserta un nuevo rol en la base de datos a partir de su nombre descriptivo.
     *
     * @param nombre Nombre del nuevo rol a crear.
     * @return {@code true} si el rol fue creado y guardado satisfactoriamente; {@code false} en caso de error.
     */
    public boolean insertRol(String nombre) {
        try {
            RolModel nuevo = new RolModel(nombre);
            RolModel rolGuardado = repository.save(nuevo);
            return rolGuardado.getIdRol() > 0;
        } catch (Exception e) {
            return false;
        } 
    }
}
