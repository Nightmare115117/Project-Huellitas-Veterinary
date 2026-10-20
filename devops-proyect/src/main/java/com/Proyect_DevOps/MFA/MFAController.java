package com.Proyect_DevOps.MFA;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.services.UsuarioService;
import com.Proyect_DevOps.utilities.AESUtil;
import com.Proyect_DevOps.utilities.JWTUtil;

/**
 * Controlador REST para la gestión de autenticación multifactor (MFA/TOTP).
 * Proporciona endpoints para habilitar, confirmar, verificar y deshabilitar MFA.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@RestController
@RequestMapping("/usuario/mfa")
public class MFAController {

    @Autowired
    private MFAService mfaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Inicia el proceso de habilitación de MFA para el usuario autenticado.
     * Genera un secreto TOTP pendiente, lo cifra con AES y lo almacena temporalmente.
     * Devuelve la URI otpauth:// para que el usuario escanee el código QR.
     *
     * @param auth Token JWT del usuario autenticado (token de acceso completo).
     * @return URI otpauth:// y secreto pendiente para escanear.
     */
    @PostMapping("/setup")
    public ResponseEntity<Map<String, Object>> setupMFA(JwtAuthenticationToken auth) {
        String correo = auth.getToken().getSubject();
        UsuarioModel usuario = usuarioService.buscarPorCorreo(correo);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        if (usuario.getSecretoMFA() != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "MFA ya está habilitado para este usuario"));
        }

        GoogleAuthenticatorKey key = mfaService.generarSecreto();
        String secreto = key.getKey();
        String uriQR = mfaService.generarUriQR(correo, key);

        // Guardar secreto pendiente cifrado con AES
        usuario.setSecretoPendienteMFA(AESUtil.encriptar(secreto));
        usuarioService.actualizarMFA(usuario);

        return ResponseEntity.ok(Map.of(
                "qrUri", uriQR,
                "secreto", secreto,
                "mensaje", "Escanee el código QR con su aplicación de autenticación y confirme con un código"
        ));
    }

    /**
     * Confirma la habilitación de MFA validando el primer código TOTP generado por el usuario.
     * Si es válido, mueve el secreto pendiente al secreto activo y activa la bandera MFA.
     *
     * @param body Mapa con el campo "codigo" (código TOTP de 6 dígitos).
     * @param auth Token JWT del usuario autenticado.
     * @return Confirmación de que MFA fue habilitado exitosamente.
     */
    @PostMapping("/confirm")
    public ResponseEntity<Map<String, Object>> confirmarMFA(
            @RequestBody Map<String, String> body,
            JwtAuthenticationToken auth) {

        String correo = auth.getToken().getSubject();
        String codigoStr = body.get("codigo");

        if (codigoStr == null || codigoStr.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El código es requerido"));
        }

        int codigo;
        try {
            codigo = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El código debe ser numérico"));
        }

        UsuarioModel usuario = usuarioService.buscarPorCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        if (usuario.getSecretoPendienteMFA() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No hay una configuración MFA pendiente. Use /mfa/setup primero"));
        }

        String secretoPendiente = AESUtil.desencriptar(usuario.getSecretoPendienteMFA());

        if (!mfaService.verificarCodigo(secretoPendiente, codigo)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Código MFA incorrecto"));
        }

        // Activar MFA: mover secreto pendiente al activo
        usuario.setSecretoMFA(usuario.getSecretoPendienteMFA());
        usuario.setSecretoPendienteMFA(null);
        usuario.setMFANeeded(true);
        usuarioService.actualizarMFA(usuario);

        return ResponseEntity.ok(Map.of(
                "mfaHabilitado", true,
                "mensaje", "MFA habilitado exitosamente"
        ));
    }

    /**
     * Verifica el código TOTP durante el flujo de login con MFA.
     * Requiere un token temporal (purpose=mfa) emitido durante el login inicial.
     * Si la verificación es exitosa, emite un token de acceso completo.
     *
     * @param body Mapa con el campo "codigo" (código TOTP de 6 dígitos).
     * @param auth Token JWT temporal de MFA.
     * @return Token JWT de acceso completo si la verificación es exitosa.
     */
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verificarMFA(
            @RequestBody Map<String, String> body,
            JwtAuthenticationToken auth) {

        String codigoStr = body.get("codigo");

        if (codigoStr == null || codigoStr.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("validacion", false, "error", "El código es requerido"));
        }

        int codigo;
        try {
            codigo = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("validacion", false, "error", "El código debe ser numérico"));
        }

        Jwt jwt = auth.getToken();
        String correo = jwt.getSubject();
        int rolId = jwt.getClaim("role_id");
        String rolNombre = jwt.getClaimAsString("roles");

        UsuarioModel usuario = usuarioService.buscarPorCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("validacion", false, "error", "Usuario no encontrado"));
        }

        if (usuario.getSecretoMFA() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("validacion", false, "error", "MFA no está configurado correctamente"));
        }

        String secreto = AESUtil.desencriptar(usuario.getSecretoMFA());

        if (!mfaService.verificarCodigo(secreto, codigo)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("validacion", false, "error", "Código MFA incorrecto"));
        }

        // Emitir token de acceso completo
        String token = jwtUtil.generarToken(correo, rolId, rolNombre);

        return ResponseEntity.ok(Map.of(
                "validacion", true,
                "login", true,
                "token", token
        ));
    }

    /**
     * Deshabilita MFA para el usuario autenticado tras verificar un código TOTP válido.
     *
     * @param body Mapa con el campo "codigo" (código TOTP de 6 dígitos para confirmar).
     * @param auth Token JWT del usuario autenticado (token de acceso completo).
     * @return Confirmación de que MFA fue deshabilitado.
     */
    @PostMapping("/disable")
    public ResponseEntity<Map<String, Object>> deshabilitarMFA(
            @RequestBody Map<String, String> body,
            JwtAuthenticationToken auth) {

        String correo = auth.getToken().getSubject();
        String codigoStr = body.get("codigo");

        if (codigoStr == null || codigoStr.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El código es requerido para deshabilitar MFA"));
        }

        int codigo;
        try {
            codigo = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El código debe ser numérico"));
        }

        UsuarioModel usuario = usuarioService.buscarPorCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        if (usuario.getSecretoMFA() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "MFA no está habilitado para este usuario"));
        }

        String secreto = AESUtil.desencriptar(usuario.getSecretoMFA());

        if (!mfaService.verificarCodigo(secreto, codigo)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Código MFA incorrecto"));
        }

        // Desactivar MFA
        usuario.setSecretoMFA(null);
        usuario.setSecretoPendienteMFA(null);
        usuario.setMFANeeded(false);
        usuarioService.actualizarMFA(usuario);

        return ResponseEntity.ok(Map.of(
                "mfaDeshabilitado", true,
                "mensaje", "MFA deshabilitado exitosamente"
        ));
    }
}
