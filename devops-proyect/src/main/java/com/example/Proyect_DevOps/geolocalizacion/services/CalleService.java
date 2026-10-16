package com.example.Proyect_DevOps.geolocalizacion.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.geolocalizacion.models.CalleModel;
import com.example.Proyect_DevOps.geolocalizacion.repositories.CalleRepository;

@Service
public class CalleService extends GenericServiceImpl<CalleModel, Integer, CalleRepository> {

}
