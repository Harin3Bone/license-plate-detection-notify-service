package com.dl.detectionnotifyservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Collections;

public class ApiKeyAuthConfig extends AbstractAuthenticationProcessingFilter {

    private final String apiKeyHeader;

    public ApiKeyAuthConfig(String apiKeyHeader) {
        super(request -> true);
        this.apiKeyHeader = apiKeyHeader;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String apiKey = request.getHeader(apiKeyHeader);
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = request.getParameter(apiKeyHeader);
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(apiKey, null, Collections.emptyList());
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}