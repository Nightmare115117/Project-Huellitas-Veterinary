package com.example.Proyect_DevOps.bitacora.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.Proyect_DevOps.bitacora.interceptors.BitacoraInterceptor;

/**
 * Configuración del ciclo de vida Web MVC para el registro de auditoría.
 * Registra el interceptor {@link BitacoraInterceptor} para monitorear las peticiones HTTP que transitan por la API.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Configuration
public class BitacoraWebConfig implements WebMvcConfigurer {

    /**
     * Interceptor encargado de capturar y auditar las operaciones HTTP entrantes.
     */
    private final BitacoraInterceptor bitacoraInterceptor;

    /**
     * Constructor con inyección del interceptor de bitácora.
     *
     * @param bitacoraInterceptor Interceptor de auditoría a registrar.
     */
    public BitacoraWebConfig(BitacoraInterceptor bitacoraInterceptor) {
        this.bitacoraInterceptor = bitacoraInterceptor;
    }

    /**
     * Registra el interceptor de bitácora asociándolo al patrón de rutas globales ({@code /**}).
     *
     * @param registry Registro de interceptores de Spring Web MVC.
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(bitacoraInterceptor)
                .addPathPatterns("/**");
    }
}
