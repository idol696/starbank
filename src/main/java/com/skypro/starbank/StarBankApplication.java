package com.skypro.starbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StarBankApplication {

	public static void main(String[] args) {

		SpringApplication.run(StarBankApplication.class, args);
	}

}
