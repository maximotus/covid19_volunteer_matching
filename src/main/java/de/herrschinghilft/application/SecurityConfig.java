package de.herrschinghilft.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${api.key.header}")
    private String apiKeyHeader;

    @Value("${api.key.value}")
    private String apiKeyValue;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        log.info("Got request to filter.");
        APIAuthFilter filter = new APIAuthFilter(apiKeyHeader);
        filter.setAuthenticationManager(authentication -> {
            String principal = (String) authentication.getPrincipal();
            if (!apiKeyValue.equals(principal)) {
                log.warn("Unauthorized API request. Principal used: " + principal);
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity.csrf().disable().addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }
}