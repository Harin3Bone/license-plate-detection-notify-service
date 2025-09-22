package com.dl.detectionnotifyservice.config;

import com.dl.detectionnotifyservice.properties.AuthProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import static com.dl.detectionnotifyservice.constant.ErrorMessage.AUTHORIZE_FAILED;

@Configuration
@EnableWebSecurity
@ConfigurationPropertiesScan(basePackages = "com.dl.detectionnotifyservice.properties")
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            /* Actuator */
            "/actuator/health",

            /* OpenAPI */
            "/api-docs/**",
            "/api-docs.yaml/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**"
    };

    private static final String[] AUTH_BLACKLIST = {
            "/actuator/restart"
    };

    private final AuthProperties apiKeyProperties;

    public SecurityConfig(AuthProperties apiKeyProperties) {
        this.apiKeyProperties = apiKeyProperties;
    }

    @Bean
    public ApiKeyAuthConfig apiKeyAuthFilter() {
        ApiKeyAuthConfig filter = new ApiKeyAuthConfig(apiKeyProperties.getKeyName());
        filter.setAuthenticationManager(authentication -> {
            String principle = (String) authentication.getPrincipal();
            if (!apiKeyProperties.getAuthValue().equals(principle)) {
                throw new BadCredentialsException(AUTHORIZE_FAILED);
            }
            return authentication;
        });
        return filter;
    }

    @Bean
    public SecurityFilterChain authorizeConfigure(HttpSecurity http) throws Exception {
        // Configure allow cors
        CorsConfiguration corsSetup = new CorsConfiguration();
        corsSetup.setAllowedOrigins(Collections.singletonList("*"));
        corsSetup.setAllowedHeaders(Collections.singletonList("*"));
        corsSetup.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "TRACE", "OPTIONS"));

        UrlBasedCorsConfigurationSource corsConfigure = new UrlBasedCorsConfigurationSource();
        corsConfigure.registerCorsConfiguration("/**", corsSetup);

        // Configure allow methods
        RequestMatcher csrfMatcher = new RequestMatcher() {
            private final Pattern allowedMethods = Pattern.compile("^(GET|POST|PUT|DELETE|PATCH|HEAD|TRACE|OPTIONS)$");
            private final RegexRequestMatcher apiMatcher = new RegexRequestMatcher("/v[0-9]*/.*", null);

            @Override
            public boolean matches(HttpServletRequest request) {
                return !allowedMethods.matcher(request.getMethod()).matches() &&
                       !apiMatcher.matches(request);
            }
        };

        return http
                .cors(cors -> cors.configurationSource(corsConfigure))
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(csrfMatcher))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(apiKeyAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(AUTH_BLACKLIST).denyAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}
