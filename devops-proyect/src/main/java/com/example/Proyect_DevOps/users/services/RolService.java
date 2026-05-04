package com.example.Proyect_DevOps.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Proyect_DevOps.users.repositories.RolRepository;

@Service
public class RolService {

    @Autowired
    @SuppressWarnings("unused")
    private RolRepository rolRepository;

}
