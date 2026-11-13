package com.Proyect_DevOps.common.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

class TurnstileVerifierTest {

    private static final String HOSTNAME = "dev-server.tail223158.ts.net";

    private RestTemplateBuilder builder;
    private RestTemplate restTemplate;
    private TurnstileVerifier verifier;

    @BeforeEach
    void setUp() {
        builder = mock(RestTemplateBuilder.class);
        restTemplate = mock(RestTemplate.class);
        when(builder.setConnectTimeout(any(Duration.class))).thenReturn(builder);
        when(builder.setReadTimeout(any(Duration.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        verifier = new TurnstileVerifier(builder, true, "server-secret", HOSTNAME);
    }

    @Test
    void acceptsOnlySuccessfulLoginTokenForExpectedHostname() {
        when(restTemplate.postForObject(
                eq("https://challenges.cloudflare.com/turnstile/v0/siteverify"),
                any(HttpEntity.class),
                eq(TurnstileVerifier.SiteverifyResponse.class)))
                .thenReturn(new TurnstileVerifier.SiteverifyResponse(true, HOSTNAME, "login"));

        assertTrue(verifier.verify("valid-token"));
    }

    @Test
    void rejectsMissingTokenWithoutCallingCloudflare() {
        assertFalse(verifier.verify(" "));
        verify(restTemplate, never()).postForObject(
                any(String.class), any(HttpEntity.class), eq(TurnstileVerifier.SiteverifyResponse.class));
    }

    @Test
    void rejectsDifferentHostnameOrAction() {
        when(restTemplate.postForObject(
                any(String.class),
                any(HttpEntity.class),
                eq(TurnstileVerifier.SiteverifyResponse.class)))
                .thenReturn(new TurnstileVerifier.SiteverifyResponse(true, "attacker.example", "login"));
        assertFalse(verifier.verify("token"));

        when(restTemplate.postForObject(
                any(String.class),
                any(HttpEntity.class),
                eq(TurnstileVerifier.SiteverifyResponse.class)))
                .thenReturn(new TurnstileVerifier.SiteverifyResponse(true, HOSTNAME, "other"));
        assertFalse(verifier.verify("token"));
    }

    @Test
    void failsClosedWhenVerificationServiceThrows() {
        when(restTemplate.postForObject(
                any(String.class),
                any(HttpEntity.class),
                eq(TurnstileVerifier.SiteverifyResponse.class)))
                .thenThrow(new org.springframework.web.client.ResourceAccessException("timeout"));

        assertFalse(verifier.verify("token"));
    }

    @Test
    void requiresSecretAndHostnameWhenEnabled() {
        assertThrows(IllegalStateException.class, () -> new TurnstileVerifier(builder, true, "", HOSTNAME));
        assertThrows(IllegalStateException.class, () -> new TurnstileVerifier(builder, true, "secret", ""));
    }
}
