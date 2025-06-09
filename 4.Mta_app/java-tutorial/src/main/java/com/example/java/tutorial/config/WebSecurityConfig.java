package com.example.java.tutorial.config;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    private final XsuaaServiceConfiguration xsuaaServiceConfiguration;

    /**
     * Constructor for WebSecurityConfig
     * Initializes the XsuaaServiceConfiguration object
     *
     * @param xsuaaServiceConfiguration The XsuaaServiceConfiguration object used for authentication and authorization
     */
    public WebSecurityConfig(XsuaaServiceConfiguration xsuaaServiceConfiguration) {
        this.xsuaaServiceConfiguration = xsuaaServiceConfiguration;
    }

    /**
     * Configures the security filter chain for the application
     * This method sets up the security policies and authentication mechanisms
     *
     * @param http The HttpSecurity object used to configure security settings
     * @return SecurityFilterChain object representing the configured security filter chain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").authenticated()
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(getJwtAuthoritiesConverter())
                        )
                );


        return http.build();
    }

    /**
     * Customizes how GrantedAuthority are derived from a Jwt
     *
     * @return jwt converter
     */
    Converter<Jwt, AbstractAuthenticationToken> getJwtAuthoritiesConverter() {
        TokenAuthenticationConverter converter = new TokenAuthenticationConverter(xsuaaServiceConfiguration);
        converter.setLocalScopeAsAuthorities(true);
        return converter;
    }

}
