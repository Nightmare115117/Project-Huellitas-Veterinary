package com.example.Proyect_DevOps.bitacora.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.Proyect_DevOps.bitacora.interceptors.BitacoraInterceptor;

@Configuration
public class BitacoraWebConfig implements WebMvcConfigurer {

    private final BitacoraInterceptor bitacoraInterceptor;

    public BitacoraWebConfig(BitacoraInterceptor bitacoraInterceptor) {
        this.bitacoraInterceptor = bitacoraInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(bitacoraInterceptor)
                .addPathPatterns("/**");
    }
}
