package com.Proyect_DevOps.common.security;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class PasswordPolicy {

    private static final Pattern LOWERCASE = Pattern.compile(".*[a-z].*");
    private static final Pattern UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SYMBOL = Pattern.compile(".*[^A-Za-z0-9\\s].*");

    private PasswordPolicy() {
    }

    public static boolean isStrong(String password) {
        return password != null
            && password.length() >= 12
            && password.length() <= 128
            && LOWERCASE.matcher(password).matches()
            && UPPERCASE.matcher(password).matches()
            && DIGIT.matcher(password).matches()
            && SYMBOL.matcher(password).matches();
    }

    public static void requireStrong(String password) {
        if (!isStrong(password)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La contraseña debe tener entre 12 y 128 caracteres e incluir mayúscula, minúscula, número y símbolo"
            );
        }
    }
}
