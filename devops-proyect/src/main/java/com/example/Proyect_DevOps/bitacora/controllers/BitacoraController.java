package com.example.Proyect_DevOps.bitacora.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.Proyect_DevOps.bitacora.models.BitacoraModel;
import com.example.Proyect_DevOps.bitacora.services.BitacoraService;
import com.example.Proyect_DevOps.common.controllers.GenericController;

/**
 * Controlador REST para la consulta y administración de las entradas de auditoría (bitácora).
 * Expone endpoints protegidos exclusivos para roles administrativos que permiten revisar el historial de acciones del sistema.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Tag(name = "Bitácora", description = "Consulta de la bitácora de auditoría (solo administradores)")
@RestController
@RequestMapping("/bitacora")
public class BitacoraController extends GenericController<BitacoraModel, Integer, BitacoraService> {
}
