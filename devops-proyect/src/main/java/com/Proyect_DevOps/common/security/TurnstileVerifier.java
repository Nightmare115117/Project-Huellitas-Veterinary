package com.Proyect_DevOps.common.security;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class TurnstileVerifier {

    private static final Logger log = LoggerFactory.getLogger(TurnstileVerifier.class);
    private static final String SITEVERIFY_URL = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    private final boolean enabled;
    private final String secret;
    private final String expectedHostname;
    private final RestTemplate restTemplate;

    public TurnstileVerifier(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${app.security.turnstile.enabled:false}") boolean enabled,
            @Value("${app.security.turnstile.secret:}") String secret,
            @Value("${app.security.turnstile.expected-hostname:}") String expectedHostname) {
        this.enabled = enabled;
        this.secret = secret;
        this.expectedHostname = expectedHostname;
        if (enabled && (!StringUtils.hasText(secret) || !StringUtils.hasText(expectedHostname))) {
            throw new IllegalStateException(
                    "TURNSTILE_SECRET y TURNSTILE_EXPECTED_HOSTNAME son obligatorios cuando Turnstile está habilitado");
        }
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(2))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }

    public boolean verify(String token) {
        if (!enabled) {
            return true;
        }
        if (token == null || token.isBlank() || token.length() > 2048) {
            return false;
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secret);
        form.add("response", token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            SiteverifyResponse response = restTemplate.postForObject(
                    SITEVERIFY_URL,
                    new HttpEntity<>(form, headers),
                    SiteverifyResponse.class);
            return response != null
                    && response.success()
                    && expectedHostname.equalsIgnoreCase(response.hostname())
                    && "login".equals(response.action());
        } catch (RestClientException exception) {
            log.warn("No se pudo verificar el token de Turnstile: {}", exception.getClass().getSimpleName());
            return false;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record SiteverifyResponse(boolean success, String hostname, String action) {
    }
}
