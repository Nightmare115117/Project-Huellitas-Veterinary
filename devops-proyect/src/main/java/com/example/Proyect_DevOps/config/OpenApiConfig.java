package com.example.Proyect_DevOps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuración central de OpenAPI 3 y Swagger UI para el Sistema Veterinario Huellitas.
 * Define la metadata de la API REST, información de contacto, esquemas de seguridad
 * y habilita la autenticación con tokens JWT Bearer en la interfaz interactiva.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    /**
     * Define la especificación OpenAPI de la aplicación con soporte para autenticación Bearer JWT.
     *
     * @return Objeto {@link OpenAPI} configurado con la metadata general y componentes de seguridad.
     */
    @Bean
    public OpenAPI huellitasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Veterinario Huellitas - API REST")
                        .description("Documentación interactiva de la API REST para el Sistema Veterinario Huellitas. "
                                + "Permite administrar citas, mascotas, consultas médicas, tratamientos, usuarios, sucursales, auditoría y geolocalización.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Soporte Huellitas DevOps")
                                .email("soporte@huellitas.vet"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese su token JWT generado en el endpoint POST /usuario/login para autorizar las solicitudes.")));
    }
}
