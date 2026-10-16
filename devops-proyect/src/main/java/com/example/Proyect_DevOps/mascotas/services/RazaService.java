package com.example.Proyect_DevOps.mascotas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.mascotas.models.EspecieModel;
import com.example.Proyect_DevOps.mascotas.models.RazaModel;
import com.example.Proyect_DevOps.mascotas.repositories.EspecieRepository;
import com.example.Proyect_DevOps.mascotas.repositories.RazaRepository;

@Service
public class RazaService extends GenericServiceImpl<RazaModel, Integer, RazaRepository> {

    @Autowired
    private EspecieRepository especieRepository;

    public List<RazaModel> findByEspecieId(int idEspecie) {
        return especieRepository.findById(idEspecie)
                .map(repository::findByEspecie)
                .orElse(List.of());
    }
}
