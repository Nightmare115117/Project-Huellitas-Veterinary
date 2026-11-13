package com.Proyect_DevOps.common.security;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

public final class ResourceAccessPolicy {

    private static final Set<String> STAFF_AUTHORITIES = Set.of(
        "Admin",
        "Administrador",
        "Gestor",
        "GESTOR",
        "Veterinario",
        "VETERINARIO"
    );

    private ResourceAccessPolicy() {
    }

    public static boolean isStaff(Authentication authentication) {
        return authentication != null
            && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(STAFF_AUTHORITIES::contains);
    }

    public static void requireUserOrStaff(String correo, Authentication authentication) {
        if (correo == null || correo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Correo requerido");
        }

        if (!isStaff(authentication)
                && (authentication == null || !authentication.getName().equalsIgnoreCase(correo))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado para consultar estos datos");
        }
    }
}
