package com.example.onlinechat.filter;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;

import java.util.List;

public class STOMPAuthenticationFilter implements ChannelInterceptor {
    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            final List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
            final Authentication authentication =
                    authorizationHeaders == null || authorizationHeaders.isEmpty() ? null
                            : JWTVerifier.verifyAuthorizationHeader(authorizationHeaders.get(0));
            accessor.setUser(authentication);
        }
        if (accessor != null && !StompCommand.CONNECT.equals(accessor.getCommand())
                && accessor.getUser() == null)
            return null;
        return message;
    }
}
