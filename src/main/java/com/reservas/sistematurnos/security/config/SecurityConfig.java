package com.reservas.sistematurnos.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/usuarios").permitAll()
                        .requestMatchers(HttpMethod.POST, "/profesionales").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/profesionales").permitAll()
                        .requestMatchers(HttpMethod.GET, "/profesionales").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/profesionales").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Forma moderna

        return http.build();
    }
}
