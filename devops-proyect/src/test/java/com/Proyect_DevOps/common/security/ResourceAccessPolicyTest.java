package com.Proyect_DevOps.common.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

class ResourceAccessPolicyTest {

    @Test
    void allowsOwnerToAccessOwnRecords() {
        Authentication authentication = authentication("cliente@example.com", "TOKEN_access");

        assertDoesNotThrow(() ->
            ResourceAccessPolicy.requireUserOrStaff("cliente@example.com", authentication)
        );
    }

    @Test
    void deniesOwnerAccessToAnotherUsersRecords() {
        Authentication authentication = authentication("cliente@example.com", "TOKEN_access");

        ResponseStatusException exception = assertThrows(
            ResponseStatusException.class,
            () -> ResourceAccessPolicy.requireUserOrStaff("otro@example.com", authentication)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void allowsAuthorizedStaffToAccessOtherUsersRecords() {
        Authentication authentication = authentication("vet@example.com", "TOKEN_access", "Veterinario");

        assertDoesNotThrow(() ->
            ResourceAccessPolicy.requireUserOrStaff("cliente@example.com", authentication)
        );
    }

    private Authentication authentication(String name, String... authorities) {
        return new UsernamePasswordAuthenticationToken(
            name,
            "token",
            java.util.Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .toList()
        );
    }
}
