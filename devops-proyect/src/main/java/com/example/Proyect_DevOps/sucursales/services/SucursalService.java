package com.example.Proyect_DevOps.sucursales.services;

import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.common.services.GenericServiceImpl;
import com.example.Proyect_DevOps.sucursales.models.SucursalModel;
import com.example.Proyect_DevOps.sucursales.repositories.SucursalRepository;

@Service
public class SucursalService extends GenericServiceImpl<SucursalModel, Integer, SucursalRepository> {

}
