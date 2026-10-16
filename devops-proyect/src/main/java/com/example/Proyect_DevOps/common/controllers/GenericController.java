package com.example.Proyect_DevOps.common.controllers;

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

import com.example.Proyect_DevOps.common.services.GenericService;

public abstract class GenericController<T, ID, S extends GenericService<T, ID>> {

    @Autowired
    protected S service;

    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<T> getById(@PathVariable("id") ID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        T created = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<T> update(@PathVariable("id") ID id, @RequestBody T entity) {
        return service.update(id, entity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<Void> delete(@PathVariable("id") ID id) {
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
