package com.otp.verification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoginOtpApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginOtpApplication.class, args);
	}

}