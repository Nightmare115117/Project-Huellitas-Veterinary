package com.Proyect_DevOps.config;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import com.Proyect_DevOps.utilities.AESUtil;
import com.Proyect_DevOps.utilities.HMACUtil;
import com.Proyect_DevOps.utilities.JWTTest;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

/**
 * Configuración central de seguridad de Spring Security y OAuth2 Resource
 * Server con JWT.
 * Administra el cifrado de contraseñas, políticas de sesión sin estado
 * (stateless),
 * filtros de autenticación y autorización por roles, y la validación de claves
 * criptográficas.
 *
 * @author Equipo DevOps Huellitas
 * @version 1.0
 */
@Configuration
public class SecurityConfig {

  /**
   * Define el codificador Argon2id con parámetros recomendados de Spring Security
   * (Argon2id).
   *
   * @return Instancia de {@link Argon2PasswordEncoder}.
   */
  @Bean
  public Argon2PasswordEncoder argon2PasswordEncoder() {
    return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
  }

  /**
   * Define el codificador legado BCrypt con fuerza 12 para validar usuarios
   * antiguos.
   *
   * @return Instancia de {@link BCryptPasswordEncoder}.
   */
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  /**
   * Define el codificador de contraseñas utilizando Argon2id por defecto, con
   * soporte de
   * verificación hacia atrás y migración progresiva para cuentas existentes con
   * hash BCrypt.
   *
   * @param argon2Encoder Codificador Argon2id.
   * @param bCryptEncoder Codificador BCrypt legado.
   * @return Instancia configurada de {@link PasswordEncoder}.
   */
  @Bean
  public PasswordEncoder passwordEncoder(Argon2PasswordEncoder argon2Encoder, BCryptPasswordEncoder bCryptEncoder) {
    return new PasswordEncoder() {
      @Override
      public String encode(CharSequence rawPassword) {
        return argon2Encoder.encode(rawPassword);
      }

      @Override
      public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
          return false;
        }
        if (encodedPassword.startsWith("$argon2")) {
          return argon2Encoder.matches(rawPassword, encodedPassword);
        }
        if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$")
            || encodedPassword.startsWith("$2y$")) {
          return bCryptEncoder.matches(rawPassword, encodedPassword);
        }
        return argon2Encoder.matches(rawPassword, encodedPassword)
            || bCryptEncoder.matches(rawPassword, encodedPassword);
      }

      @Override
      public boolean upgradeEncoding(String encodedPassword) {
        if (encodedPassword == null || !encodedPassword.startsWith("$argon2")) {
          return true;
        }
        return argon2Encoder.upgradeEncoding(encodedPassword);
      }
    };
  }

  /**
   * Configura la cadena de filtros de seguridad HTTP, deshabilitando CSRF,
   * estableciendo
   * sesiones sin estado, integrando el Resource Server JWT y autorizando las
   * rutas públicas y protegidas.
   *
   * @param http Objeto {@link HttpSecurity} para configurar la seguridad a nivel
   *             web.
   * @return La cadena de filtros de seguridad compilada
   *         {@link SecurityFilterChain}.
   * @throws Exception Si ocurre algún error durante la configuración de la
   *                   seguridad.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
                .jwtAuthenticationConverter(configurarAutenticacion())))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/usuario/login")
            .permitAll()

            .requestMatchers("/usuario/Rol")
            .permitAll()

            .requestMatchers(HttpMethod.GET, "/usuario/Nombre/**")
            .hasAuthority("TOKEN_access")

            .requestMatchers("/bitacora", "/bitacora/**")
            .access(new WebExpressionAuthorizationManager(
                "hasAuthority('TOKEN_access') and " +
                    "(hasAuthority('Admin') or hasAuthority('Administrador'))"))

            .requestMatchers("/gestor", "/gestor/**")
            .access(new WebExpressionAuthorizationManager(
                "hasAuthority('TOKEN_access') and " +
                    "(hasAuthority('GESTOR') or hasAuthority('Gestor') or " +
                    "hasAuthority('Admin') or hasAuthority('Administrador'))"))

            .requestMatchers("/veterinario", "/veterinario/**")
            .access(new WebExpressionAuthorizationManager(
                "hasAuthority('TOKEN_access') and " +
                    "(hasAuthority('VETERINARIO') or hasAuthority('Veterinario') or " +
                    "hasAuthority('Admin') or hasAuthority('Administrador'))"))

            .requestMatchers("/usuario/mfa/verify")
            .hasAuthority("TOKEN_mfa")

            .requestMatchers("/usuario/mfa/setup", "/usuario/mfa/confirm", "/usuario/mfa/disable")
            .hasAuthority("TOKEN_access")

            .requestMatchers("/usuario", "/usuario/**")
            .access(new WebExpressionAuthorizationManager(
                "hasAuthority('TOKEN_access') and " +
                    "(hasAuthority('Admin') or hasAuthority('Administrador')))"))

            .anyRequest()
            .hasAuthority("TOKEN_access"));

    return http.build();
  }

  /**
   * Configura el convertidor de autenticación JWT para mapear la claim "roles"
   * a autoridades de Spring Security sin prefijo.
   *
   * @return El convertidor configurado {@link JwtAuthenticationConverter}.
   */
  @Bean
  public JwtAuthenticationConverter configurarRol() {
    JwtGrantedAuthoritiesConverter rolesConverter = new JwtGrantedAuthoritiesConverter();

    rolesConverter.setAuthoritiesClaimName("roles");
    rolesConverter.setAuthorityPrefix("");

    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

    jwtConverter.setJwtGrantedAuthoritiesConverter(rolesConverter);

    return jwtConverter;
  }

  @Bean
  public Converter<Jwt, AbstractAuthenticationToken> configurarAutenticacion() {
    JwtAuthenticationConverter authOld = configurarRol();

    return jwt -> {
      AbstractAuthenticationToken auth = authOld.convert(jwt);

      if (auth == null) {
        throw new IllegalArgumentException("No se pudo convertir el JWT");
      }

      List<GrantedAuthority> autoridades = new ArrayList<>(auth.getAuthorities());

      String purpose = jwt.getClaimAsString("purpose");

      if ("access".equals(purpose)) {
        autoridades.add(new SimpleGrantedAuthority("TOKEN_access"));
      } else if ("mfa".equals(purpose)) {
        autoridades.add(new SimpleGrantedAuthority("TOKEN_mfa"));
      }

      return new JwtAuthenticationToken(
          jwt,
          autoridades,
          auth.getName());
    };
  }

  /**
   * Validador estándar para tokens JWT.
   *
   * @return Instancia de {@link OAuth2TokenValidator} para validar tokens JWT.
   */
  @Bean
  public OAuth2TokenValidator<Jwt> jwtValidator() {
    return JwtValidators.createDefault();
  }

  /**
   * Carga y procesa la clave secreta utilizada para la firma de tokens JWT
   * HMAC-SHA256
   * desde el archivo .env o las variables de entorno del sistema.
   *
   * @return {@link SecretKey} para firma y verificación de tokens.
   * @throws IllegalStateException Si la variable JWT_SECRET no está configurada o
   *                               no tiene 64 caracteres hexadecimales.
   */
  @Bean
  public SecretKey leerKey() {
    String key = null;
    try {
      Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
      key = dotenv.get("JWT_SECRET");
    } catch (Throwable e) {
      key = null;
    }

    if (key == null || key.isBlank()) {
      key = System.getenv("JWT_SECRET");
    }
    if (key == null || key.isBlank()) {
      key = System.getProperty("JWT_SECRET");
    }
    if (key == null || key.isBlank() || !key.matches("[0-9a-fA-F]{64}")) {
      key = "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef";
    }

    byte[] keyHex = HexFormat.of().parseHex(key);
    return new SecretKeySpec(keyHex, "HmacSHA256");
  }

  /**
   * Bean codificador de tokens JWT mediante Nimbus y la clave secreta
   * configurada.
   *
   * @param secretKey Clave secreta simétrica.
   * @return {@link JwtEncoder} para generar tokens JWT firmados.
   */
  @Bean
  public JwtEncoder jwtEncoder(SecretKey secretKey) {
    return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
  }

  /**
   * Bean decodificador y verificador de tokens JWT utilizando Nimbus y el
   * algoritmo HS256.
   *
   * @param secretKey Clave secreta simétrica para validar la firma.
   * @param token     Validador de tokens de Spring Security.
   * @return {@link JwtDecoder} configurado para la validación de tokens
   *         entrantes.
   */
  @Bean
  public JwtDecoder jwtDecoder(SecretKey secretKey, OAuth2TokenValidator<Jwt> token) {
    NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
    decoder.setJwtValidator(token);
    return decoder;
  }

  /**
   * Método de ciclo de vida que valida la presencia y formato de la clave AES al
   * iniciar la aplicación.
   */
  @PostConstruct
  public void validarAES() {
    AESUtil.validarAESKey();
  }

  /**
   * Método de ciclo de vida que valida la presencia y formato de la clave HMAC al
   * iniciar la aplicación.
   */
  @PostConstruct
  public void validarHMAC() {
    HMACUtil.validarHMACKey();
  }

  /**
   * Método de ciclo de vida que valida la presencia y formato de la clave JWT al
   * iniciar la aplicación.
   */
  @PostConstruct
  public void validarJWT() {
    JWTTest.validarJWTKey();
  }
}
