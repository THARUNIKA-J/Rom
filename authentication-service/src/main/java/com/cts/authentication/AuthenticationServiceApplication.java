package com.cts.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cts.authentication.entity.User;
import com.cts.authentication.repository.UserRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationServiceApplication implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public AuthenticationServiceApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userRepository.save(new User("KrishnaMohan", passwordEncoder.encode("krim"), "ROLE_USER"));
		userRepository.save(new User("Tharunika", passwordEncoder.encode("thar"), "ROLE_USER"));
		userRepository.save(new User("Harika", passwordEncoder.encode("hari"), "ROLE_USER"));
		userRepository.save(new User("Hrishikesh", passwordEncoder.encode("hris"), "ROLE_USER"));
	}
}
