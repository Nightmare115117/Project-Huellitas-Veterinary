package com.example.Proyect_DevOps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.Proyect_DevOps.utilities.AESUtil;
import com.example.Proyect_DevOps.utilities.HMACUtil;

import jakarta.annotation.PostConstruct;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @PostConstruct
    public void validarAES() {
        AESUtil.validarAESKey();
    }

    @PostConstruct
    public void validarHMAC() {
        HMACUtil.validarHMACKey();
    }
}