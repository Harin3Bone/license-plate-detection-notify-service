package com.dl.detectionnotifyservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@AllArgsConstructor
public class ApiKeyAuthConfig extends AbstractPreAuthenticatedProcessingFilter {

    private String principleRequestKey;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(principleRequestKey);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

}
