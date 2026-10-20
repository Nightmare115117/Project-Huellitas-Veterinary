package com.Proyect_DevOps.common.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.Proyect_DevOps.common.services.GenericService;

/**
 * Controlador REST abstracto base que expone los endpoints CRUD genéricos.
 * Proporciona rutas estándar para listar, consultar por ID, crear, actualizar y eliminar entidades.
 *
 * @param <T> Tipo de la entidad expuesta por el controlador.
 * @param <ID> Tipo del identificador único de la entidad.
 * @param <S> Tipo del servicio asociado que implementa {@link GenericService}.
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public abstract class GenericController<T, ID, S extends GenericService<T, ID>> {

    /**
     * Servicio especializado para la gestión de las operaciones de negocio de la entidad.
     */
    @Autowired
    protected S service;

    /**
     * Endpoint HTTP GET para obtener la lista de todas las entidades.
     *
     * @return {@link ResponseEntity} con código 200 (OK) y la lista de entidades en el cuerpo.
     */
    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Endpoint HTTP GET para consultar una entidad específica por su identificador numérico.
     *
     * @param id Identificador único de la entidad proveniente de la ruta.
     * @return {@link ResponseEntity} con código 200 (OK) y la entidad encontrada,
     *         o código 404 (NOT_FOUND) si no existe.
     */
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<T> getById(@PathVariable("id") ID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Endpoint HTTP POST para crear y persistir una nueva entidad.
     *
     * @param entity Cuerpo de la petición que contiene los datos de la entidad a crear.
     * @return {@link ResponseEntity} con código 201 (CREATED) y la entidad creada.
     */
    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        T created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Endpoint HTTP PUT para actualizar una entidad existente a través de su identificador.
     *
     * @param id Identificador único de la entidad a modificar.
     * @param entity Objeto con la información actualizada proveniente del cuerpo de la petición.
     * @return {@link ResponseEntity} con código 200 (OK) y la entidad modificada,
     *         o código 404 (NOT_FOUND) si no existe.
     */
    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<T> update(@PathVariable("id") ID id, @RequestBody T entity) {
        return service.update(id, entity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Endpoint HTTP DELETE para eliminar una entidad del sistema mediante su identificador.
     *
     * @param id Identificador numérico de la entidad a eliminar.
     * @return {@link ResponseEntity} con código 204 (NO_CONTENT) si fue eliminada con éxito,
     *         o código 404 (NOT_FOUND) si no fue encontrada.
     */
    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<Void> delete(@PathVariable("id") ID id) {
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
