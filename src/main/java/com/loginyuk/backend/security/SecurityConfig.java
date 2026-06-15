package com.loginyuk.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("PASSWORD ENCODER LOADED");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/ws-chat/**").permitAll()
                    .requestMatchers("/api/test-token/**").permitAll()
                    .requestMatchers("/api/transaksi/**").permitAll()
                    .requestMatchers("/", "/*.html", "/css/**", "/js/**",
                                     "/static/css/**", "/static/js/**",
                                     "/assets/**", "/favicon.ico", "/*.png")
                    .permitAll()
                    .anyRequest().authenticated()
            );

        return http.build();
    }
}