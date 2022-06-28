package com.example.onlinechat.filter;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTVerifier jwtVerifier;

    public JWTAuthenticationFilter(JWTVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(
                jwtVerifier.verifyAuthorizationHeader(
                        request.getHeader("Authorization")
                ).orElse(null)
        );
        chain.doFilter(request, response);
    }
}
