package com.Proyect_DevOps.config;

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
     * @return Configuración de {@link WebMvcConfigurer} con las políticas de origen, métodos y credenciales.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")   // Permite cualquier origen
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);      // Permite el envío de cookies y tokens de autenticación
            }
        };
    }
}
