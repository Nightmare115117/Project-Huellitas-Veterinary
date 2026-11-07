package com.Proyect_DevOps.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Proyect_DevOps.users.models.RolModel;
import com.Proyect_DevOps.users.repositories.RolRepository;

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

    @Autowired(required = false)
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        try {
            asegurarRol(3, "Gestor");
            asegurarRol(4, "Veterinario");
        } catch (Exception e) {
            log.warn("Advertencia en inicialización de roles: {}", e.getMessage());
        }
    }

    private void asegurarRol(int id, String nombre) {
        // 1. Verificar primero mediante el repositorio JPA
        try {
            if (rolRepository.findById(id).isPresent()) {
                return;
            }
            boolean existeNombre = rolRepository.findAll().stream()
                .anyMatch(r -> nombre.equalsIgnoreCase(r.getNombre()));
            if (existeNombre) {
                return;
            }
        } catch (Exception e) {
            log.debug("No se pudo consultar mediante JPA: {}", e.getMessage());
        }

        // 2. Si no existe, insertar de forma aislada vía JDBC directo para no contaminar transacciones JPA
        if (dataSource != null) {
            try (Connection conn = dataSource.getConnection()) {
                // Determinar el nombre de columna id (id_rol o idRol)
                String idCol = "id_rol";
                try (ResultSet rs = conn.getMetaData().getColumns(null, null, "roles", "%")) {
                    while (rs.next()) {
                        String colName = rs.getString("COLUMN_NAME");
                        if ("idRol".equalsIgnoreCase(colName)) {
                            idCol = colName;
                            break;
                        }
                    }
                }

                // Verificar si ya existe por id o por nombre
                String checkSql = "SELECT COUNT(*) FROM roles WHERE " + idCol + " = ? OR LOWER(nombre) = LOWER(?)";
                try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                    ps.setInt(1, id);
                    ps.setString(2, nombre);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            return;
                        }
                    }
                }

                // Insertar el rol con el ID correspondiente
                String insertSql = "INSERT INTO roles (" + idCol + ", nombre) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setInt(1, id);
                    ps.setString(2, nombre);
                    ps.executeUpdate();
                    log.info("Rol {} ({}) insertado exitosamente vía JDBC.", nombre, id);
                    return;
                }
            } catch (Exception ex) {
                log.warn("No se pudo insertar el rol {} vía JDBC: {}", nombre, ex.getMessage());
            }
        }

        // 3. Fallback mediante JPA
        try {
            RolModel rol = new RolModel(nombre);
            rol.setIdRol(id);
            rolRepository.save(rol);
            log.info("Rol {} ({}) guardado vía JPA.", nombre, id);
        } catch (Exception ex) {
            log.warn("No se pudo guardar el rol {} vía JPA: {}", nombre, ex.getMessage());
        }
    }
}
