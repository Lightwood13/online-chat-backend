package com.example.onlinechat.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.onlinechat.config.SecurityConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

public class JWTVerifier {
    static Authentication verifyAuthorizationHeader(String authorizationHeader) {
        final String jwtToken = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.replaceFirst("Bearer ", "")
                : null;

        Authentication authentication;

        try {
            final String userName = jwtToken == null ? null :
                    JWT.require(SecurityConfig.JWT_SIGNING_ALGORITHM)
                            .build()
                            .verify(jwtToken)
                            .getSubject();

            authentication = userName == null ? null :
                    new UsernamePasswordAuthenticationToken(
                            userName,
                            jwtToken,
                            Collections.emptyList()
                    );

        } catch (JWTVerificationException e) {
            authentication = null;
        }
        return authentication;
    }
}
