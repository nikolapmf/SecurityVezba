package com.example.userservice;

import com.example.userservice.model.Role;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "AGENT"));
			userService.saveRole(new Role(null, "ADMIN"));

			userService.saveUser(new User(null, "Nikola Jovic", "dzoni", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Dalibor Ilic", "dace", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Velja Buci", "veki", "1234", new ArrayList<>()));

			userService.addRoleToUser("dzoni", "ADMIN");
			userService.addRoleToUser("dzoni", "AGENT");
			userService.addRoleToUser("dace", "AGENT");
			userService.addRoleToUser("veki", "AGENT");
		};
	}
}
