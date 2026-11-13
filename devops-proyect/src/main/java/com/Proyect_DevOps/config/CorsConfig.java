package com.Proyect_DevOps.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de Intercambio de Recursos de Origen Cruzado (CORS).
 * Habilita la comunicación con clientes front-end y aplicaciones externas permitiendo
 * métodos HTTP estándar y cabeceras personalizadas.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Configuration
public class CorsConfig {

    /**
     * Define y registra el bean de configuración CORS para toda la aplicación Web MVC.
     *
     * @return Configuración CORS con lista explícita de orígenes, métodos y cabeceras permitidos.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer(
            @Value("${app.cors.allowed-origins:http://localhost:5173,http://127.0.0.1:5173}") String allowedOrigins) {
        List<String> origins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(origins.toArray(String[]::new))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
