package com.example.onlinechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OnlineChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineChatApplication.class, args);
	}
}
