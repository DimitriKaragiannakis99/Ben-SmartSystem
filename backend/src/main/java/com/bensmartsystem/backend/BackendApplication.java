package com.bensmartsystem.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController //This class is also a rest controller
public class BackendApplication {

	//First import allows us to run application through run method where we pass BackendApplication class
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
