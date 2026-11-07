package com.Proyect_DevOps.users;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.Proyect_DevOps.MFA.MFAService;
import com.Proyect_DevOps.users.models.RolModel;
import com.Proyect_DevOps.users.models.UsuarioModel;
import com.Proyect_DevOps.users.repositories.RolRepository;
import com.Proyect_DevOps.users.repositories.UsuarioRepository;
import com.Proyect_DevOps.users.services.UsuarioService;
import com.Proyect_DevOps.utilities.HMACUtil;

public class PasswordMigrationTest {

    private UsuarioService usuarioService;
    private UsuarioRepository usuarioRepository;
    private RolRepository rolRepository;
    private MFAService mfaService;
    private Argon2PasswordEncoder argon2PasswordEncoder;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() throws Exception {
        usuarioRepository = mock(UsuarioRepository.class);
        rolRepository = mock(RolRepository.class);
        mfaService = mock(MFAService.class);
        argon2PasswordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

        usuarioService = new UsuarioService();

        setField(usuarioService, "repository", usuarioRepository);
        setField(usuarioService, "rolRepository", rolRepository);
        setField(usuarioService, "service", mfaService);
        setField(usuarioService, "argon2PasswordEncoder", argon2PasswordEncoder);
        setField(usuarioService, "bCryptPasswordEncoder", bCryptPasswordEncoder);
        setField(usuarioService, "passwordEncoder", argon2PasswordEncoder);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Class<?> current = target.getClass();
        Field field = null;
        while (current != null && field == null) {
            try {
                field = current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        if (field != null) {
            field.setAccessible(true);
            field.set(target, value);
        }
    }

    @Test
    void testLegacyBcryptLoginTriggersArgon2idMigration() {
        String rawPassword = "password123";
        String bcryptHash = bCryptPasswordEncoder.encode(rawPassword);

        UsuarioModel usuario = new UsuarioModel();
        usuario.setIdUsuario(1);
        usuario.setNombre("Carlos");
        usuario.setCorreo("test@huellitas.mx");
        usuario.setCorreoHMAC(HMACUtil.GenerarHuella("test@huellitas.mx"));
        usuario.setContraseña(bcryptHash);
        usuario.setRol(new RolModel("Gestor"));
        usuario.setMFANeeded(false);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(UsuarioModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioModel result = usuarioService.validacionDeLogin("test@huellitas.mx", rawPassword, 0);

        assertNotNull(result);
        assertTrue(result.getContraseña().startsWith("$argon2id$"),
                "El hash debe haber sido migrado a Argon2id tras el login exitoso");
        assertTrue(argon2PasswordEncoder.matches(rawPassword, result.getContraseña()),
                "El nuevo hash Argon2id debe verificar correctamente la contraseña");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testAlreadyArgon2idUserDoesNotNeedMigration() {
        String rawPassword = "password123";
        String argon2Hash = argon2PasswordEncoder.encode(rawPassword);

        UsuarioModel usuario = new UsuarioModel();
        usuario.setIdUsuario(2);
        usuario.setNombre("Luis");
        usuario.setCorreo("vet@huellitas.mx");
        usuario.setCorreoHMAC(HMACUtil.GenerarHuella("vet@huellitas.mx"));
        usuario.setContraseña(argon2Hash);
        usuario.setRol(new RolModel("Veterinario"));
        usuario.setMFANeeded(false);

        when(usuarioRepository.findByCorreoHMAC(any())).thenReturn(Optional.of(usuario));

        UsuarioModel result = usuarioService.validacionDeLogin("vet@huellitas.mx", rawPassword, 0);

        assertNotNull(result);
        assertTrue(result.getContraseña().startsWith("$argon2id$"));
        verify(usuarioRepository, times(0)).save(any(UsuarioModel.class));
    }
}
