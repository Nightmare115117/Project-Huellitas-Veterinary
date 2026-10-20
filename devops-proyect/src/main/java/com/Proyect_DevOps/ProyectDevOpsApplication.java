package com.Proyect_DevOps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de inicio para la aplicación del Sistema Veterinario DevOps.
 * Inicializa el contexto de Spring Boot, componentes, repositorios JPA y configuración de seguridad.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@SpringBootApplication
public class ProyectDevOpsApplication {

	/**
	 * Punto de entrada principal de la aplicación.
	 *
	 * @param args Argumentos de la línea de comandos pasados al iniciar el servidor.
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProyectDevOpsApplication.class, args);
	}

}
