package com.example.Proyect_DevOps.users.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.users.models.RolModel;
import com.example.Proyect_DevOps.users.repositories.RolRepository;

@Service
public class RolService extends GenericServiceImpl<RolModel, Integer, RolRepository> {

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
