package com.example.Proyect_DevOps.bitacora.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.bitacora.models.BitacoraModel;
import com.example.Proyect_DevOps.bitacora.services.BitacoraService;
import com.example.Proyect_DevOps.common.controllers.GenericController;

@RestController
@RequestMapping("/bitacora")
public class BitacoraController extends GenericController<BitacoraModel, Integer, BitacoraService> {
}
