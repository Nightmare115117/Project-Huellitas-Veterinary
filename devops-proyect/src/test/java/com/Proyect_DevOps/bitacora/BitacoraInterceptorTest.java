package com.Proyect_DevOps.bitacora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.Proyect_DevOps.bitacora.interceptors.BitacoraInterceptor;
import com.Proyect_DevOps.bitacora.services.BitacoraService;

public class BitacoraInterceptorTest {

    private BitacoraService bitacoraService;
    private BitacoraInterceptor interceptor;

    @BeforeEach
    void setUp() {
        bitacoraService = mock(BitacoraService.class);
        interceptor = new BitacoraInterceptor(bitacoraService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testResolverModulo() {
        assertEquals("Citas", interceptor.resolverModulo("/cita/crear"));
        assertEquals("Mascotas", interceptor.resolverModulo("/mascota/1"));
        assertEquals("Usuarios", interceptor.resolverModulo("/usuario/5"));
        assertEquals("Tratamientos", interceptor.resolverModulo("/tratamiento"));
        assertEquals("Geolocalización", interceptor.resolverModulo("/ciudad"));
    }

    @Test
    void testResolverAccion() {
        assertEquals("CREAR_CITA", interceptor.resolverAccion("POST", "/cita/crear"));
        assertEquals("CANCELAR_CITA", interceptor.resolverAccion("PUT", "/cita/cancelar/3"));
        assertEquals("CREAR_MASCOTA", interceptor.resolverAccion("POST", "/mascota"));
        assertEquals("ACTUALIZAR_USUARIO", interceptor.resolverAccion("PUT", "/usuario/10"));
        assertEquals("ELIMINAR_SERVICIO", interceptor.resolverAccion("DELETE", "/servicio/2"));
    }

    @Test
    void testIgnoraGetYOptions() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mascota");
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.afterCompletion(request, response, new Object(), null);

        verify(bitacoraService, never()).registrarAccion(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());

        request = new MockHttpServletRequest("OPTIONS", "/cita");
        interceptor.afterCompletion(request, response, new Object(), null);

        verify(bitacoraService, never()).registrarAccion(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testIgnoraBitacoraYLogin() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/bitacora");
        MockHttpServletResponse response = new MockHttpServletResponse();
        interceptor.afterCompletion(request, response, new Object(), null);

        verify(bitacoraService, never()).registrarAccion(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());

        request = new MockHttpServletRequest("POST", "/usuario/login");
        interceptor.afterCompletion(request, response, new Object(), null);

        verify(bitacoraService, never()).registrarAccion(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testRegistraAccionMutanteConUsuarioAutenticado() {
        // Mock SecurityContext with JWT
        Jwt jwt = mock(Jwt.class);
        when(jwt.hasClaim("roles")).thenReturn(true);
        when(jwt.getClaimAsString("roles")).thenReturn("Administrador");

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("admin@huellitas.com");
        when(auth.getPrincipal()).thenReturn(jwt);
        org.mockito.Mockito.doReturn(Collections.singletonList(new SimpleGrantedAuthority("Administrador")))
                .when(auth).getAuthorities();

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/cita/crear");
        request.setRemoteAddr("192.168.1.100");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(200);

        interceptor.afterCompletion(request, response, new Object(), null);

        verify(bitacoraService).registrarAccion(
                eq("admin@huellitas.com"),
                eq("Administrador"),
                eq("CREAR_CITA"),
                eq("Citas"),
                eq("Método: POST | Ruta: /cita/crear | Resultado: exitoso"),
                eq("192.168.1.100")
        );
    }

    @Test
    void testRegistraAccionConError() {
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/usuario/99");
        request.setRemoteAddr("10.0.0.5");
        MockHttpServletResponse response = new MockHttpServletResponse();
        response.setStatus(404);

        interceptor.afterCompletion(request, response, new Object(), new RuntimeException("Usuario no encontrado"));

        verify(bitacoraService).registrarAccion(
                eq("Anónimo"),
                eq("Desconocido"),
                eq("ELIMINAR_USUARIO"),
                eq("Usuarios"),
                eq("Método: DELETE | Ruta: /usuario/99 | Resultado: código HTTP 404 | Error: Usuario no encontrado"),
                eq("10.0.0.5")
        );
    }
}
