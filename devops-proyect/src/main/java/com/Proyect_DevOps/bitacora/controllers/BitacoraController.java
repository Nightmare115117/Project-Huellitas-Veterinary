package com.Proyect_DevOps.bitacora.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Proyect_DevOps.bitacora.models.BitacoraModel;
import com.Proyect_DevOps.bitacora.services.BitacoraService;
import com.Proyect_DevOps.common.controllers.GenericController;

/**
 * Controlador REST para la consulta y administración de las entradas de auditoría (bitácora).
 * Expone endpoints protegidos exclusivos para roles administrativos que permiten revisar el historial de acciones del sistema.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/bitacora")
public class BitacoraController extends GenericController<BitacoraModel, Integer, BitacoraService> {
}
