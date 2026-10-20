package com.Proyect_DevOps.MFA;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

class MFAServiceTest {

    private final MFAService service = new MFAService();

    @Test
    void generatesSecretAndAuthenticatorUri() {
        GoogleAuthenticatorKey key = service.generarSecreto();
        String secret = key.getKey();
        String uri = service.generarUriQR("user@example.com", key);

        assertFalse(secret.isBlank());
        assertTrue(uri.startsWith("otpauth://totp/"));
        assertTrue(uri.contains("secret=" + secret));
    }

    @Test
    void verifiesCurrentCodeAndRejectsOutOfRangeCode() {
        GoogleAuthenticatorKey key = service.generarSecreto();
        String secret = key.getKey();
        int code = new GoogleAuthenticator().getTotpPassword(secret);

        assertTrue(service.verificarCodigo(secret, code));
        assertFalse(service.verificarCodigo(secret, -1));
        assertFalse(service.verificarCodigo(secret, 1_000_000));
    }
}
