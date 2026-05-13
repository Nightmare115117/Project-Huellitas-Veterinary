package com.example.Proyect_DevOps.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Proyect_DevOps.users.models.RolModel;

@Repository
public interface RolRepository extends JpaRepository<RolModel, Integer>{

}
