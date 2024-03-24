package com.bensmartsystem.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RestController //This class is also a rest controller
@EnableScheduling
public class BackendApplication {

	//First import allows us to run application through run method where we pass BackendApplication class
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
