package com.example.Proyect_DevOps.common.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Id;

public abstract class GenericServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements GenericService<T, ID> {

    @Autowired
    protected R repository;

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public Optional<T> update(ID id, T entity) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        setEntityId(entity, id);
        return Optional.of(repository.save(entity));
    }

    @Override
    @Transactional
    public boolean deleteById(ID id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    /**
     * Asigna el identificador a la entidad buscando el campo con la anotación @Id.
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
