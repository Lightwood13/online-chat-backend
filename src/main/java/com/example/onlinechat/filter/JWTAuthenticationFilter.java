package com.example.onlinechat.filter;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(
                JWTVerifier.verifyAuthorizationHeader(
                        request.getHeader("Authorization"))
        );
        chain.doFilter(request, response);
    }
}
