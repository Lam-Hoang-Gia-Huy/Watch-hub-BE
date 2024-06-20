package com.example.JWTImplemenation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtImplemenationApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtImplemenationApplication.class, args);
	}

}
