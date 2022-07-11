package com.example.onlinechat.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.onlinechat.config.SecurityConfig;
import com.example.onlinechat.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class JWTVerifier {

    private final UserService userService;

    public JWTVerifier(UserService userService) {
        this.userService = userService;
    }

    Optional<Authentication> verifyAuthorizationHeader(String authorizationHeader) {
        final Optional<String> jwtToken = Optional.ofNullable(authorizationHeader)
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.replaceFirst("Bearer ", ""));

        final Optional<String> username = jwtToken.map(token -> {
                    try {
                        return JWT.require(SecurityConfig.JWT_SIGNING_ALGORITHM)
                                .build()
                                .verify(token)
                                .getSubject();
                    } catch (JWTVerificationException e) {
                        return null;
                    }
                }
        )/*.filter(name -> userService.findUserByUsername(name).isPresent())*/;

        return username.map(name -> new UsernamePasswordAuthenticationToken(
                name,
                jwtToken,
                Collections.emptyList()
        ));
    }
}
