package com.example.Proyect_DevOps.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.users.models.RolModel;
import com.example.Proyect_DevOps.users.repositories.RolRepository;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public boolean insertRol(String nombre) {
        try {
            RolModel nuevo = new RolModel(nombre);
            RolModel rolGuardado = rolRepository.save(nuevo);
            return rolGuardado.getIdRol() > 0;
        } catch (Exception e) {
            return false;
        } 
    }

}
