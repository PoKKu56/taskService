package ru.cinimex.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.cinimex")
public class TaskserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskserviceApplication.class, args);
	}

}
