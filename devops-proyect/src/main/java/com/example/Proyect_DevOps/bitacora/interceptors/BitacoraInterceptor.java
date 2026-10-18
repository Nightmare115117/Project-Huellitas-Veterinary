package com.example.Proyect_DevOps.bitacora.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.Proyect_DevOps.bitacora.services.BitacoraService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interceptor de peticiones HTTP para la auditoría automática del sistema.
 * Captura las operaciones mutantes (POST, PUT, DELETE, PATCH), extrae el usuario autenticado
 * desde el contexto de seguridad Spring Security, mapea el módulo y la acción correspondiente,
 * y delega el registro al servicio {@link BitacoraService}.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Component
public class BitacoraInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BitacoraInterceptor.class);

    /**
     * Bandera para habilitar o deshabilitar el registro de peticiones de solo lectura (GET).
     * Por defecto se omiten para evitar saturar el almacenamiento de auditoría con tráfico rutinario.
     */
    private static final boolean REGISTRAR_CONSULTAS_GET = false;

    /**
     * Servicio de bitácora inyectado para la persistencia de eventos.
     */
    private final BitacoraService bitacoraService;

    /**
     * Constructor con inyección opcional del servicio de bitácora.
     *
     * @param bitacoraService Instancia de {@link BitacoraService} o nulo si no está disponible.
     */
    public BitacoraInterceptor(@Autowired(required = false) BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    /**
     * Callback invocado tras la finalización completa de la petición HTTP y renderizado de la vista/respuesta.
     * Realiza el filtrado de rutas técnicas y guarda el evento de auditoría.
     *
     * @param request Petición HTTP recibida.
     * @param response Respuesta HTTP generada.
     * @param handler Manejador o controlador ejecutado.
     * @param ex Posible excepción lanzada durante el procesamiento, o nula si fue exitoso.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        if (bitacoraService == null) {
            return;
        }

        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 1. Omitir peticiones preflight CORS o HEAD
        if ("OPTIONS".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method)) {
            return;
        }

        // 2. Omitir consultas de lectura (GET) según configuración
        if (!REGISTRAR_CONSULTAS_GET && "GET".equalsIgnoreCase(method)) {
            return;
        }

        // 3. Omitir rutas técnicas, endpoints de error y consultas a la propia bitácora
        if (uri.startsWith("/bitacora") || uri.startsWith("/error") || uri.startsWith("/actuator")
                || uri.contains("favicon")) {
            return;
        }

        // 4. Omitir login de usuario porque ya se registra con detalle específico en UsuarioController
        if (uri.equals("/usuario/login") || uri.startsWith("/usuario/login/")) {
            return;
        }

        try {
            // Extraer usuario y rol autenticado desde el contexto de seguridad
            String usuario = "Anónimo";
            String rol = "Desconocido";

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                String name = auth.getName();
                if (name != null && !name.isBlank()) {
                    usuario = name;
                }

                if (auth.getPrincipal() instanceof Jwt jwt) {
                    if (jwt.hasClaim("roles")) {
                        String claimRol = jwt.getClaimAsString("roles");
                        if (claimRol != null && !claimRol.isBlank()) {
                            rol = claimRol;
                        }
                    }
                }

                if ("Desconocido".equals(rol) && !auth.getAuthorities().isEmpty()) {
                    rol = auth.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(a -> a != null && !a.isBlank())
                            .findFirst()
                            .orElse("Usuario");
                }
            }

            // Extraer IP del cliente
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            } else {
                ip = ip.split(",")[0].trim();
            }
            if (ip == null || ip.isBlank()) {
                ip = "Desconocida";
            }

            // Determinar módulo y acción
            String modulo = resolverModulo(uri);
            String accion = resolverAccion(method, uri);

            // Determinar detalle del resultado
            int status = response.getStatus();
            String estadoHttp = (status >= 200 && status < 300) ? "exitoso" : "código HTTP " + status;
            String detalle = String.format("Método: %s | Ruta: %s | Resultado: %s", method, uri, estadoHttp);
            if (ex != null && ex.getMessage() != null) {
                detalle += " | Error: " + ex.getMessage();
            }

            // Guardar en la bitácora
            bitacoraService.registrarAccion(usuario, rol, accion, modulo, detalle, ip);

        } catch (Exception e) {
            // Evitar que cualquier fallo de auditoría interrumpa la respuesta del sistema
            log.warn("No se pudo registrar la acción en la bitácora para URI {}: {}", uri, e.getMessage());
        }
    }

    /**
     * Resuelve el nombre del módulo del negocio examinando la ruta URI de la petición.
     *
     * @param uri Ruta de la solicitud HTTP.
     * @return Nombre legible del módulo asociado (ej. "Citas", "Mascotas", "Geolocalización").
     */
    public String resolverModulo(String uri) {
        String cleanUri = uri.toLowerCase();
        if (cleanUri.contains("/cita")) return "Citas";
        if (cleanUri.contains("/mascota")) return "Mascotas";
        if (cleanUri.contains("/especie")) return "Especies";
        if (cleanUri.contains("/raza")) return "Razas";
        if (cleanUri.contains("/tratamiento")) return "Tratamientos";
        if (cleanUri.contains("/consulta")) return "Consultas Médicas";
        if (cleanUri.contains("/usuario")) return "Usuarios";
        if (cleanUri.contains("/rol")) return "Roles";
        if (cleanUri.contains("/servicio")) return "Servicios";
        if (cleanUri.contains("/sucursal")) return "Sucursales";
        if (cleanUri.contains("/gestor")) return "Gestores";
        if (cleanUri.contains("/direccion") || cleanUri.contains("/calle") || cleanUri.contains("/ciudad")
                || cleanUri.contains("/colonia") || cleanUri.contains("/estado") || cleanUri.contains("/pais")) {
            return "Geolocalización";
        }

        String[] partes = uri.split("/");
        for (String p : partes) {
            if (!p.isBlank()) {
                return Character.toUpperCase(p.charAt(0)) + p.substring(1).toLowerCase();
            }
        }
        return "General";
    }

    /**
     * Resuelve el código descriptivo de la acción realizada con base en el método HTTP y la ruta URI.
     *
     * @param method Método HTTP utilizado (GET, POST, PUT, DELETE, etc.).
     * @param uri Ruta URI de la petición.
     * @return Código de la acción normalizado (ej. "CREAR_CITA", "ACTUALIZAR_USUARIO").
     */
    public String resolverAccion(String method, String uri) {
        String upperMethod = method != null ? method.toUpperCase() : "ACCION";
        String lowerUri = uri != null ? uri.toLowerCase() : "";

        // Casos de negocio específicos
        if (lowerUri.contains("/cita/cancelar")) {
            return "CANCELAR_CITA";
        }
        if (lowerUri.contains("/cita/crear")) {
            return "CREAR_CITA";
        }
        if (lowerUri.contains("/rol/insert")) {
            return "CREAR_ROL";
        }

        // Acciones CRUD según el recurso
        String entidad = extraerEntidad(lowerUri);

        return switch (upperMethod) {
            case "POST" -> "CREAR_" + entidad;
            case "PUT" -> "ACTUALIZAR_" + entidad;
            case "DELETE" -> "ELIMINAR_" + entidad;
            case "PATCH" -> "MODIFICAR_" + entidad;
            case "GET" -> "CONSULTAR_" + entidad;
            default -> upperMethod + "_" + entidad;
        };
    }

    /**
     * Deduce el nombre de la entidad principal a partir de los segmentos de la ruta URI.
     *
     * @param uri Ruta en minúsculas.
     * @return Nombre de la entidad en mayúsculas (ej. "CITA", "MASCOTA", "USUARIO").
     */
    private String extraerEntidad(String uri) {
        if (uri.contains("/cita")) return "CITA";
        if (uri.contains("/mascota")) return "MASCOTA";
        if (uri.contains("/especie")) return "ESPECIE";
        if (uri.contains("/raza")) return "RAZA";
        if (uri.contains("/tratamiento")) return "TRATAMIENTO";
        if (uri.contains("/consulta")) return "CONSULTA_MEDICA";
        if (uri.contains("/usuario")) return "USUARIO";
        if (uri.contains("/rol")) return "ROL";
        if (uri.contains("/servicio")) return "SERVICIO";
        if (uri.contains("/sucursal")) return "SUCURSAL";
        if (uri.contains("/gestor")) return "GESTOR";
        if (uri.contains("/direccion")) return "DIRECCION";
        if (uri.contains("/calle")) return "CALLE";
        if (uri.contains("/ciudad")) return "CIUDAD";
        if (uri.contains("/colonia")) return "COLONIA";
        if (uri.contains("/estado")) return "ESTADO";
        if (uri.contains("/pais")) return "PAIS";

        String[] partes = uri.split("/");
        for (String p : partes) {
            if (!p.isBlank() && !p.matches("\\d+")) {
                return p.toUpperCase();
            }
        }
        return "REGISTRO";
    }
}
