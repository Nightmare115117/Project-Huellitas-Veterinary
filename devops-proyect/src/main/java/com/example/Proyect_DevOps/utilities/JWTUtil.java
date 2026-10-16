package com.example.Proyect_DevOps.utilities;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JWTUtil {

    private final JwtEncoder encoder;

    public JWTUtil (JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generarToken(String username, int rol, String rolname) {

        java.time.Instant ahora = java.time.Instant.now();
        java.time.Instant expiracion = ahora.plus(java.time.Duration.ofMinutes(15));

        JwtClaimsSet claims = JwtClaimsSet.builder()
        .subject(username)
        .issuedAt(ahora)
        .expiresAt(expiracion)
        .claim("role_id", rol)
        .claim("roles", rolname)
        .build();

        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);

        Jwt result = encoder.encode(parameters);
        return result.getTokenValue();
    }
}