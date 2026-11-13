package com.Proyect_DevOps.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;

import com.Proyect_DevOps.bitacora.services.BitacoraService;
import com.Proyect_DevOps.common.controllers.GenericController;
import com.Proyect_DevOps.common.security.TurnstileVerifier;
import com.Proyect_DevOps.users.controllers.UsuarioController;
import com.Proyect_DevOps.users.services.UsuarioService;
import com.Proyect_DevOps.utilities.JWTUtil;

class UsuarioControllerCaptchaTest {

    @Test
    void refusesLoginWhenCaptchaVerificationFails() throws Exception {
        TurnstileVerifier verifier = mock(TurnstileVerifier.class);
        when(verifier.verify("invalid-token")).thenReturn(false);
        BitacoraService bitacora = mock(BitacoraService.class);
        UsuarioService usuarioService = mock(UsuarioService.class);
        UsuarioController controller = new UsuarioController(mock(JWTUtil.class), bitacora, verifier);
        setService(controller, usuarioService);

        var response = controller.login(
                Map.of("correo", "user@example.com", "contrasena", "StrongPass1!", "captchaToken", "invalid-token"),
                new MockHttpServletRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(usuarioService, never()).validacionDeLogin(anyString(), anyString(), eq(0));
        verify(bitacora).registrarAccion(
                eq("user@example.com"),
                eq("Desconocido"),
                eq("LOGIN_FALLIDO"),
                eq("Autenticación"),
                eq("Verificación CAPTCHA fallida."),
                eq("127.0.0.1"));
    }

    private static void setService(UsuarioController controller, UsuarioService service) throws Exception {
        Field field = GenericController.class.getDeclaredField("service");
        field.setAccessible(true);
        field.set(controller, service);
    }
}
