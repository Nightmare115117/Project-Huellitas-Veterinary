package com.example.Proyect_DevOps.consultaMedica.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.consultaMedica.models.ConsultaMedicaModel;
import com.example.Proyect_DevOps.consultaMedica.repositories.ConsultaMedicaRepository;

@Service
public class ConsultaMedicaService extends GenericServiceImpl<ConsultaMedicaModel, Integer, ConsultaMedicaRepository> {

}
