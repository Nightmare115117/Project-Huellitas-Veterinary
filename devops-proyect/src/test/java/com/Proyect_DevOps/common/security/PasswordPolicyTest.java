package com.Proyect_DevOps.common.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordPolicyTest {

    @Test
    void acceptsLongPasswordWithAllRequiredCharacterClasses() {
        assertTrue(PasswordPolicy.isStrong("Veterinary2026!"));
    }

    @Test
    void rejectsShortOrLowComplexityPasswords() {
        assertFalse(PasswordPolicy.isStrong("Password1!"));
        assertFalse(PasswordPolicy.isStrong("veterinary2026!"));
        assertFalse(PasswordPolicy.isStrong("VeterinaryClinic"));
    }
}
