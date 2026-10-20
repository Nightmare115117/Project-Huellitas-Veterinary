package com.Proyect_DevOps.common.services;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica base para las operaciones CRUD en la capa de servicios.
 * Define el contrato estándar para la manipulación y consulta de entidades del sistema.
 *
 * @param <T> Tipo de la entidad gestionada por el servicio.
 * @param <ID> Tipo del identificador único de la entidad.
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public interface GenericService<T, ID> {

    /**
     * Recupera todas las entidades almacenadas en el sistema.
     *
     * @return Lista con la totalidad de entidades encontradas.
     */
    List<T> findAll();

    /**
     * Busca una entidad específica a partir de su identificador único.
     *
     * @param id Identificador único de la entidad a buscar.
     * @return {@link Optional} que contiene la entidad si fue encontrada, o vacío en caso contrario.
     */
    Optional<T> findById(ID id);

    /**
     * Persiste una nueva entidad o actualiza una existente en el almacén de datos.
     *
     * @param entity Objeto de la entidad a guardar.
     * @return La entidad guardada con sus valores actualizados o generados.
     */
    T save(T entity);

    /**
     * Actualiza los datos de una entidad existente identificada por su ID.
     *
     * @param id Identificador único de la entidad a actualizar.
     * @param entity Objeto con los nuevos datos a asignar a la entidad.
     * @return {@link Optional} con la entidad actualizada, o vacío si el identificador no existe.
     */
    Optional<T> update(ID id, T entity);

    /**
     * Elimina una entidad del almacén de datos basándose en su identificador.
     *
     * @param id Identificador único de la entidad a eliminar.
     * @return {@code true} si la entidad existía y fue eliminada correctamente; {@code false} si no existía.
     */
    boolean deleteById(ID id);

    /**
     * Comprueba si existe una entidad registrada con el identificador proporcionado.
     *
     * @param id Identificador único a verificar.
     * @return {@code true} si existe una entidad con dicho ID, {@code false} de lo contrario.
     */
    boolean existsById(ID id);
}
