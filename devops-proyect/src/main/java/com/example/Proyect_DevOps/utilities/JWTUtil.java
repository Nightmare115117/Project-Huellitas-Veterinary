package com.example.Proyect_DevOps.utilities;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de la generación y firma de tokens de autenticación JWT (JSON Web Tokens).
 * Incorpora información de identidad del usuario, identificador de rol, nombre del rol
 * y tiempo de expiración predeterminado de 15 minutos.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class JWTUtil {

    /**
     * Codificador de tokens JWT inyectado por Spring Security OAuth2.
     */
    private final JwtEncoder encoder;

    /**
     * Constructor para inyección de dependencias del codificador JWT.
     *
     * @param encoder Codificador {@link JwtEncoder} configurado en el contexto.
     */
    public JWTUtil(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Genera un token JWT firmado mediante HS256 para el usuario autenticado.
     *
     * @param correo Correo electrónico del usuario (utilizado como sujeto del token).
     * @param rol Identificador numérico del rol asignado.
     * @param rolname Nombre descriptivo del rol (por ejemplo, "Admin", "Veterinario", "Cliente").
     * @return Cadena que representa el token JWT codificado y firmado.
     */
    public String generarToken(String correo, int rol, String rolname) {

        java.time.Instant ahora = java.time.Instant.now();
        java.time.Instant expiracion = ahora.plus(java.time.Duration.ofMinutes(15));

        JwtClaimsSet claims = JwtClaimsSet.builder()
        .subject(correo)
        .issuedAt(ahora)
        .expiresAt(expiracion)
        .claim("role_id", rol)
        .claim("roles", rolname)
        .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtEncoderParameters parameters = JwtEncoderParameters.from(header, claims);

        Jwt result = encoder.encode(parameters);
        return result.getTokenValue();
    }
}
