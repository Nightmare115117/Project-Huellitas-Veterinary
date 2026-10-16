package com.example.Proyect_DevOps.mascotas.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.mascotas.models.EspecieModel;
import com.example.Proyect_DevOps.mascotas.repositories.EspecieRepository;

@Service
public class EspecieService extends GenericServiceImpl<EspecieModel, Integer, EspecieRepository> {

    public List<EspecieModel> mostrarEspecies(){
        return findAll();
    }
}
