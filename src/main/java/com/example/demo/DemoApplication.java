package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@EntityScan("com.example.demo.persistence.model")
@SpringBootApplication 
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println("xys");
		SpringApplication.run(DemoApplication.class, args);
	}
}
