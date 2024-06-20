package com.example.JWTImplemenation.Config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration{
    private final JwtAuthenticationFilter JwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("api/v1/auth/**")
                    .permitAll()
                    .requestMatchers("/ws/**").permitAll()
                    .requestMatchers("/api/v1/watch", "/api/v1/watch/{id}").permitAll()
                    .requestMatchers("/api/v1/user", "/api/v1/user/{id}").permitAll()
                    .requestMatchers("/api/v1/appraisal", "/api/v1/appraisal/{id}").permitAll()
                    .anyRequest().authenticated()
            ).authenticationProvider(authenticationProvider)
            .addFilterBefore(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class );
    return http.build();
}
}
