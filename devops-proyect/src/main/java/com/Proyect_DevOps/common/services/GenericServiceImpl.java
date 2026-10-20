package com.Proyect_DevOps.common.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Id;

/**
 * Implementación base abstracta de la interfaz {@link GenericService}.
 * Proporciona la lógica predeterminada para operaciones CRUD utilizando un repositorio JPA,
 * con soporte para transacciones y asignación reflexiva del identificador durante actualizaciones.
 *
 * @param <T> Tipo de la entidad gestionada.
 * @param <ID> Tipo del identificador único de la entidad.
 * @param <R> Tipo del repositorio JPA asociado a la entidad.
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public abstract class GenericServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements GenericService<T, ID> {

    /**
     * Repositorio JPA inyectado para la persistencia y consulta de la entidad.
     */
    @Autowired
    protected R repository;

    /**
     * {@inheritDoc}
     * Realiza una consulta de solo lectura para obtener todos los registros.
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     * Realiza una consulta de solo lectura por identificador primario.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     * Persiste la entidad dentro de una transacción activa.
     */
    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    /**
     * {@inheritDoc}
     * Verifica la existencia previa del registro y asigna el identificador antes de guardar.
     */
    @Override
    @Transactional
    public Optional<T> update(ID id, T entity) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        setEntityId(entity, id);
        return Optional.of(repository.save(entity));
    }

    /**
     * {@inheritDoc}
     * Elimina el registro por su identificador si se encuentra presente.
     */
    @Override
    @Transactional
    public boolean deleteById(ID id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    /**
     * {@inheritDoc}
     * Comprueba en solo lectura si existe el identificador en la base de datos.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    /**
     * Asigna reflexivamente el identificador proporcionado al campo anotado con {@link Id}
     * dentro de la jerarquía de clases de la entidad.
     *
     * @param entity Instancia de la entidad a modificar.
     * @param id Identificador que se inyectará en la entidad.
     */
    protected void setEntityId(T entity, ID id) {
        if (entity == null || id == null) {
            return;
        }
        Class<?> current = entity.getClass();
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        if (field.getType().equals(int.class) && id instanceof Number) {
                            field.setInt(entity, ((Number) id).intValue());
                        } else if (field.getType().equals(Integer.class) && id instanceof Number) {
                            field.set(entity, Integer.valueOf(((Number) id).intValue()));
                        } else if (field.getType().equals(long.class) && id instanceof Number) {
                            field.setLong(entity, ((Number) id).longValue());
                        } else if (field.getType().equals(Long.class) && id instanceof Number) {
                            field.set(entity, Long.valueOf(((Number) id).longValue()));
                        } else {
                            field.set(entity, id);
                        }
                        return;
                    } catch (Exception e) {
                        // Continuar buscando si ocurre algún error
                    }
                }
            }
            current = current.getSuperclass();
        }
    }
}
