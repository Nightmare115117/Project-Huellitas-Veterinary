package com.example.Proyect_DevOps.bitacora.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.Proyect_DevOps.bitacora.models.BitacoraModel;
import com.example.Proyect_DevOps.bitacora.repositories.BitacoraRepository;
import com.example.Proyect_DevOps.common.services.GenericServiceImpl;

@Service
public class BitacoraService extends GenericServiceImpl<BitacoraModel, Integer, BitacoraRepository> {

    private static final int MAX_DETALLE_LEN = 990;

    @Override
    @Transactional(readOnly = true)
    public List<BitacoraModel> findAll() {
        return repository.findAllByOrderByFechaHoraDesc();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BitacoraModel registrarAccion(String usuario, String rol, String accion, String modulo, String detalle, String ip) {
        String cleanUsuario = (usuario != null && !usuario.isBlank()) ? usuario : "Anónimo";
        String cleanRol = (rol != null && !rol.isBlank()) ? rol : "Desconocido";
        String cleanAccion = (accion != null && !accion.isBlank()) ? accion : "ACCION";
        String cleanModulo = (modulo != null && !modulo.isBlank()) ? modulo : "General";
        String cleanIp = (ip != null && !ip.isBlank()) ? ip : "Desconocida";
        String cleanDetalle = sanitizeDetalle(detalle);

        BitacoraModel bitacora = new BitacoraModel(cleanUsuario, cleanRol, cleanAccion, cleanModulo, cleanDetalle, cleanIp);
        return repository.save(bitacora);
    }

    @Override
    @Transactional
    public BitacoraModel save(BitacoraModel entity) {
        if (entity.getFechaHora() == null) {
            entity.setFechaHora(LocalDateTime.now());
        }
        if (entity.getDetalle() != null) {
            entity.setDetalle(sanitizeDetalle(entity.getDetalle()));
        }
        return super.save(entity);
    }

    private String sanitizeDetalle(String detalle) {
        if (detalle == null) return null;
        if (detalle.length() > MAX_DETALLE_LEN) {
            return detalle.substring(0, MAX_DETALLE_LEN) + "...";
        }
        return detalle;
    }
}
