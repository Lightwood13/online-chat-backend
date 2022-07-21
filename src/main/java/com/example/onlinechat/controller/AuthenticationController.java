package com.example.onlinechat.controller;

import com.example.onlinechat.service.UserService;
import com.example.onlinechat.service.dto.LoginCredentialsDTO;
import com.example.onlinechat.service.dto.SignUpCredentialsDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final UserService userService;

    AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginCredentialsDTO credentials) {
        return userService.loginUser(credentials);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody SignUpCredentialsDTO credentials) {
        return userService.signupUser(credentials);
    }
}
