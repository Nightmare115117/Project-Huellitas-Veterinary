package com.Proyect_DevOps.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.Proyect_DevOps.users.models.RolModel;
import com.Proyect_DevOps.users.repositories.RolRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Inicializador para asegurar la existencia de los roles requeridos en la base de datos,
 * específicamente el rol Veterinario (id 4) y el rol Gestor (id 3).
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Component
public class RoleDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RoleDataInitializer.class);

    @Autowired
    private RolRepository rolRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        asegurarRol(3, "Gestor");
        asegurarRol(4, "Veterinario");
    }

    private void asegurarRol(int id, String nombre) {
        if (rolRepository.findById(id).isEmpty()) {
            boolean nombreExiste = rolRepository.findAll().stream()
                .anyMatch(r -> nombre.equalsIgnoreCase(r.getNombre()));

            if (!nombreExiste) {
                try {
                    entityManager.createNativeQuery("INSERT INTO roles (idRol, nombre) VALUES (:id, :nombre)")
                        .setParameter("id", id)
                        .setParameter("nombre", nombre)
                        .executeUpdate();
                    log.info("Rol {} ({}) insertado exitosamente con idRol.", nombre, id);
                } catch (Exception e1) {
                    try {
                        entityManager.createNativeQuery("INSERT INTO roles (id_rol, nombre) VALUES (:id, :nombre)")
                            .setParameter("id", id)
                            .setParameter("nombre", nombre)
                            .executeUpdate();
                        log.info("Rol {} ({}) insertado exitosamente con id_rol.", nombre, id);
                    } catch (Exception e2) {
                        try {
                            RolModel rol = new RolModel(nombre);
                            rol.setIdRol(id);
                            rolRepository.save(rol);
                            log.info("Rol {} ({}) guardado mediante JPA.", nombre, id);
                        } catch (Exception ex) {
                            log.warn("No se pudo insertar automáticamente el rol {} (id {}): {}", nombre, id, ex.getMessage());
                        }
                    }
                }
            }
        }
    }
}
