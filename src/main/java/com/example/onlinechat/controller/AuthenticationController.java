package com.example.onlinechat.controller;

import com.example.onlinechat.service.dto.LoginCredentialsDTO;
import com.example.onlinechat.service.UserService;
import com.example.onlinechat.service.dto.SignUpCredentialsDTO;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final UserService userService;

    AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping("/login")
    public String login(@RequestBody LoginCredentialsDTO credentials) {
        return userService.loginUser(credentials);
    }

    @CrossOrigin
    @PostMapping("/signup")
    public String signup(@RequestBody SignUpCredentialsDTO credentials) {
        return userService.signupUser(credentials);
    }

}
