package com.example.Proyect_DevOps.mascotas.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Proyect_DevOps.common.controllers.GenericController;
import com.example.Proyect_DevOps.mascotas.dtos.MascotaDTO;
import com.example.Proyect_DevOps.mascotas.models.MascotaModel;
import com.example.Proyect_DevOps.mascotas.services.MascotaService;

@RestController
@RequestMapping("/mascota")
public class MascotaController extends GenericController<MascotaModel, Integer, MascotaService> {

    @GetMapping("/{correo:.+@.+}")
    public List<MascotaDTO> getListMascota(@PathVariable String correo) {
        return service.getAllMascotaByUser(correo);
    }
    
    @GetMapping("/usuario/{correo:.+}")
    public long contarMascotasPorUsuario(@PathVariable String correo) {
        return service.getCountMascotasByUser(correo);
    }
}
