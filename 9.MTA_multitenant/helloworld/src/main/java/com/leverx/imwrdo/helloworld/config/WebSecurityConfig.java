package com.leverx.imwrdo.helloworld.config;


import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    private static final String API_V1_CALLBACK = "/callback/v1.0/tenants/*";
    private static final String API_V1_PATTERN = "/api/v1/**";
    private final XsuaaServiceConfiguration xsuaaServiceConfiguration;

    @Bean
    @Profile("cloud")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API_V1_CALLBACK).permitAll()
                        .requestMatchers(API_V1_PATTERN).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(getJwtConverter())));

        return http.build();
    }

    @Bean
    @Profile("local")
    public SecurityFilterChain filterChainLocal(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth ->
                auth.anyRequest().permitAll()
        );

        return http.build();
    }

    private Converter<Jwt, AbstractAuthenticationToken> getJwtConverter() {
        TokenAuthenticationConverter converter = new TokenAuthenticationConverter(xsuaaServiceConfiguration);
        converter.setLocalScopeAsAuthorities(true);
        return converter;
    }

}
