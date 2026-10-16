package com.example.Proyect_DevOps.common.services;

import java.util.List;
import java.util.Optional;

public interface GenericService<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    Optional<T> update(ID id, T entity);
    boolean deleteById(ID id);
    boolean existsById(ID id);
}
