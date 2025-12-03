package com.scoring.github.popularity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class PopularityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PopularityServiceApplication.class, args);
	}

}
