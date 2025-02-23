package com.grupo05.coworking_space;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
@EnableAspectJAutoProxy
public class CoworkingSpaceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoworkingSpaceApplication.class, args);
	}
}
