package com.mediscreen.massessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.mediscreen.massessment")
public class MassessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MassessmentApplication.class, args);
	}

}
