package com.example.hirezo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class HirezoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HirezoApplication.class, args);
	}
	@GetMapping("/hello")
	public String hello() {
		return "Hello, World!";
	}
}
