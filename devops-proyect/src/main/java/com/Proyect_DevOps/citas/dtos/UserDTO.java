package com.Proyect_DevOps.citas.dtos;

/**
 * Objeto de transferencia de datos (DTO) para el registro y consulta simplificada de usuarios en citas.
 *
 * @param nombre Nombre de pila del usuario.
 * @param paterno Apellido paterno.
 * @param materno Apellido materno.
 * @param correo Correo electrónico del usuario.
 * @param contraseña Contraseña o clave del usuario.
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public record UserDTO ( 
    String nombre,
    String paterno,
    String materno,
    String correo,
    String contraseña
) {}
