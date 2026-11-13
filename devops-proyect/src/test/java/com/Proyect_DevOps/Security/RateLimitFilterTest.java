package com.Proyect_DevOps.Security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.Proyect_DevOps.Security.Filter.RateLimitFilter;
import com.Proyect_DevOps.Security.Service.RateLimitService;

import jakarta.servlet.ServletException;

class RateLimitFilterTest {

    @Test
    void limitsRequestsIndependentlyPerClient() {
        RateLimitService rateLimitService = new RateLimitService(1, 60);

        assertTrue(rateLimitService.tryConsume("192.0.2.1"));
        assertFalse(rateLimitService.tryConsume("192.0.2.1"));
        assertTrue(rateLimitService.tryConsume("192.0.2.2"));
    }

    @Test
    void returnsTooManyRequestsWhenClientExceedsLimit() throws ServletException, IOException {
        RateLimitFilter filter = new RateLimitFilter(new RateLimitService(1, 60));
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/mascota");
        request.setRemoteAddr("192.0.2.1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicInteger chainInvocations = new AtomicInteger();

        filter.doFilter(request, response, (servletRequest, servletResponse) -> chainInvocations.incrementAndGet());
        filter.doFilter(request, response, (servletRequest, servletResponse) -> chainInvocations.incrementAndGet());

        assertEquals(429, response.getStatus());
        assertEquals("60", response.getHeader("Retry-After"));
        assertEquals(1, chainInvocations.get());
    }

    @Test
    void doesNotConsumeTokensForCorsPreflightRequests() throws ServletException, IOException {
        RateLimitFilter filter = new RateLimitFilter(new RateLimitService(1, 60));
        MockHttpServletRequest optionsRequest = new MockHttpServletRequest("OPTIONS", "/mascota");
        optionsRequest.setRemoteAddr("192.0.2.1");
        MockHttpServletResponse optionsResponse = new MockHttpServletResponse();
        AtomicInteger optionsChainInvocations = new AtomicInteger();

        filter.doFilter(optionsRequest, optionsResponse,
                (servletRequest, servletResponse) -> optionsChainInvocations.incrementAndGet());

        MockHttpServletRequest getRequest = new MockHttpServletRequest("GET", "/mascota");
        getRequest.setRemoteAddr("192.0.2.1");
        MockHttpServletResponse getResponse = new MockHttpServletResponse();
        AtomicInteger getChainInvocations = new AtomicInteger();
        filter.doFilter(getRequest, getResponse,
                (servletRequest, servletResponse) -> getChainInvocations.incrementAndGet());

        assertEquals(1, optionsChainInvocations.get());
        assertEquals(1, getChainInvocations.get());
        assertEquals(200, getResponse.getStatus());
    }
}
