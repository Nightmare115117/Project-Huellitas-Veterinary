package com.example.Proyect_DevOps.utilities;

/**
 * Enumeración que define los códigos o causas de error comunes vinculados
 * a la validación de tokens JWT y control de acceso en la capa de seguridad.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public enum ErrorsUtils {
    /**
     * El token JWT proporcionado no tiene una firma o estructura válida.
     */
    JWT_INVALIDO,

    /**
     * La fecha de expiración del token JWT ya ha vencido.
     */
    JWT_EXPIRADO,

    /**
     * La petición no incluye la cabecera de autorización o el token JWT requerido.
     */
    JWT_AUSENTE,

    /**
     * El usuario autenticado no posee los permisos o roles necesarios para acceder al recurso.
     */
    ACCESO_DENEGADO,
}
