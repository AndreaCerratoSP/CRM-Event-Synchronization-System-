package com.crm.sync.ingestion.config;

import com.crm.sync.ingestion.filter.ApiKeyFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** 
 * Security configuration class for the CRM ingestion API.
 **/

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;
    
    /**
	 * Configures the security filter chain for the application.
	 * Disables CSRF protection, adds the custom API key filter before the username/password authentication filter,
	 * and requires authentication for all requests.
	 *
	 * @param http the HttpSecurity object to configure
	 * @return the configured SecurityFilterChain
	 * @throws Exception if an error occurs while configuring the security filter chain
	 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                   .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
                   .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                   .build();
    }
}
