package com.example.onlinechat.service;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

public class AuthenticationService {

    // TODO: refactor other services to use this method
    public static UUID getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Principal::getName)
                .map(UUID::fromString)
                .orElseThrow();
    }
}
