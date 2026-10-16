package com.example.Proyect_DevOps.config;

import java.util.HexFormat;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import com.example.Proyect_DevOps.utilities.AESUtil;
import com.example.Proyect_DevOps.utilities.HMACUtil;
import com.example.Proyect_DevOps.utilities.JWTTest;

import jakarta.annotation.PostConstruct;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(configurarRol())
                )
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()
                .requestMatchers("/usuario").hasAuthority("Admin")
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter configurarRol() {
        JwtGrantedAuthoritiesConverter rolesConverter =
            new JwtGrantedAuthoritiesConverter();

        rolesConverter.setAuthoritiesClaimName("roles");
        rolesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtConverter =
            new JwtAuthenticationConverter();

        jwtConverter.setJwtGrantedAuthoritiesConverter(rolesConverter);

        return jwtConverter;
    }

    @Bean
    public OAuth2TokenValidator<Jwt> jwtValidator() {
        return JwtValidators.createDefault();
    }

    @Bean 
    public SecretKey leerKey(){
        String key;
        try {
            Dotenv dotenv = Dotenv.load();
            key = dotenv.get("JWT_SECRET");
        } catch (DotenvException e) {
            key = System.getenv("JWT_SECRET");
        }

        if (key == null || key.isEmpty() || !key.matches("[0-9a-fA-F]{64}")) 
            throw new IllegalStateException("No se pudo optener el JWT_SECRET");

        byte[] keyHex = HexFormat.of().parseHex(key);
        return new SecretKeySpec(keyHex, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey secretKey) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    @Bean 
    public JwtDecoder jwtDecoder(SecretKey secretKey, OAuth2TokenValidator<Jwt> token) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
        decoder.setJwtValidator(token);
        return decoder;
    }

    @PostConstruct
    public void validarAES() {
        AESUtil.validarAESKey();
    }

    @PostConstruct
    public void validarHMAC() {
        HMACUtil.validarHMACKey();
    }

    @PostConstruct
    public void validarJWT() {
        JWTTest.validarJWTKey();
    }
}