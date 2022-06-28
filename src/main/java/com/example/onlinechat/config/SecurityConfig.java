package com.example.onlinechat.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private static final String JWT_SECRET = "IdZfukrXjPMh7Cfytgn5J8S9sEU9g2qQ";
    public static final Algorithm JWT_SIGNING_ALGORITHM = Algorithm.HMAC512(JWT_SECRET);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
