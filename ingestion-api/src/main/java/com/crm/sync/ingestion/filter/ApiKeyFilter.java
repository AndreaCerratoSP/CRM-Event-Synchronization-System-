package com.crm.sync.ingestion.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filter that validates the API key for incoming HTTP requests to the CRM ingestion API.
 */
@Component
@Slf4j
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${crm.api.key}")
    private String storedHash;

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor injection for PasswordEncoder.
     *
     * @param passwordEncoder the BCrypt password encoder
     */
    public ApiKeyFilter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
	 * Filters incoming HTTP requests to validate the presence and correctness of the API key.
	 * If the API key is missing or invalid, responds with a 401 Unauthorized status.
	 * If the API key is valid, sets the authentication in the security context and continues the filter chain.
	 *
	 * @param request the incoming HTTP request
	 * @param response the HTTP response to be sent back to the client
	 * @param filterChain the filter chain to pass the request
	 * @throws ServletException if an error occurs during filtering
	 * @throws IOException if an I/O error occurs during filtering
	 **/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
        String requestApiKey = request.getHeader("X-API-KEY");
        if (requestApiKey == null) {
            requestApiKey = request.getHeader("x-api-key");
        }
        
        log.debug("Received API Key header: {}", requestApiKey != null ? "Present" : "Missing");

        if (requestApiKey == null) {
            log.warn("Missing API Key header");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Missing API Key header");
            return;
        }

        // inputPassword è il valore in chiaro (requestApiKey), mentre storedHash è il valore cifrato
        // Lasciamo fare il match all'encoder come richiesto
        if (!passwordEncoder.matches(requestApiKey.trim(), storedHash)) {
            log.warn("Invalid API Key provided");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid API Key");
            return;
        }

        log.info("Authentication successful for API Key");
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken("CRM", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
