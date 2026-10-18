package com.example.Proyect_DevOps.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.users.models.UsuarioModel;

/**
 * Repositorio JPA para la persistencia y búsqueda de entidades {@link UsuarioModel}.
 * Permite la búsqueda segura por huella criptográfica HMAC del correo.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    
    /**
     * Busca un usuario por la huella digital criptográfica (HMAC-SHA256) de su correo electrónico.
     *
     * @param correo Huella HMAC del correo a buscar.
     * @return {@link Optional} con el usuario correspondiente, o vacío si no se encuentra.
     */
    Optional<UsuarioModel> findByCorreoHMAC(String correo);

    /**
     * Comprueba si ya existe un usuario registrado con la huella digital del correo especificado.
     *
     * @param correo Huella HMAC del correo a validar.
     * @return {@code true} si ya existe un registro con dicha huella, {@code false} en caso contrario.
     */
    boolean existsByCorreoHMAC(String correo);
}
