package com.Proyect_DevOps.MFA;

import org.springframework.stereotype.Service;

import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

/**
 * Servicio para la gestión de autenticación multifactor (MFA) mediante TOTP
 * utilizando la librería Google Authenticator.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Service
public class MFAService {

    private final GoogleAuthenticator auth = new GoogleAuthenticator();

    /**
     * Genera un nuevo secreto TOTP representado por {@link GoogleAuthenticatorKey}.
     *
     * @return Clave generada por GoogleAuthenticator.
     */
    public GoogleAuthenticatorKey generarSecreto() {
        return auth.createCredentials();
    }

    /**
     * Genera la URI otpauth:// para que el usuario la escanee con su app TOTP
     * (Google Authenticator, Authy, etc.).
     *
     * @param cuenta Identificador de la cuenta del usuario (ej. correo).
     * @param secreto Instancia de {@link GoogleAuthenticatorKey}.
     * @return URI otpauth:// lista para generar un código QR.
     */
    public String generarUriQR(String cuenta, GoogleAuthenticatorKey secreto) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("Huellitas", cuenta, secreto);
    }

    /**
     * Verifica un código TOTP contra el secreto del usuario.
     *
     * @param secreto Secreto TOTP en formato Base32.
     * @param codigo Código de 6 dígitos introducido por el usuario.
     * @return {@code true} si el código es válido, {@code false} en caso contrario.
     */
    public boolean verificarCodigo(String secreto, int codigo) {
        return auth.authorize(secreto, codigo);
    }
}
