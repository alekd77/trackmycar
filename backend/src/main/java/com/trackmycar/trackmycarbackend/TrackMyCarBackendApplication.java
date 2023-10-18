package com.trackmycar.trackmycarbackend;

import com.trackmycar.trackmycarbackend.entity.AppUser;
import com.trackmycar.trackmycarbackend.entity.Role;
import com.trackmycar.trackmycarbackend.repository.RoleRepository;
import com.trackmycar.trackmycarbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TrackMyCarBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackMyCarBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository,
						  UserRepository userRepository,
						  PasswordEncoder passwordEncoder) {
		return args -> {
			if (roleRepository.findByAuthority("ADMIN").isPresent()) {
				return;
			}

			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			AppUser admin = new AppUser(
					1,
					"admin",
					passwordEncoder.encode("password"),
					"admin@gmail.com",
					"Admin",
					roles
			);

			userRepository.save(admin);
		};
	}

}
