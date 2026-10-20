package com.Proyect_DevOps;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Inicializador de servlets para habilitar el despliegue de la aplicación como archivo WAR
 * tradicional en servidores de aplicaciones externos (como Tomcat, Jetty o GlassFish).
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
	 * Configura la aplicación especificando la fuente principal de Spring Boot.
	 *
	 * @param application Constructor de la aplicación Spring.
	 * @return El constructor configurado con la clase de inicio.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ProyectDevOpsApplication.class);
	}

}
