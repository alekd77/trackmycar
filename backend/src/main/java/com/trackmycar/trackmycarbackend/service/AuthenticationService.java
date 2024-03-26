package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.model.AppUser;
import com.trackmycar.trackmycarbackend.model.Role;
import com.trackmycar.trackmycarbackend.exception.AuthenticationFailedException;
import com.trackmycar.trackmycarbackend.exception.InvalidInputException;
import com.trackmycar.trackmycarbackend.repository.RoleRepository;
import com.trackmycar.trackmycarbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    @Autowired
    public AuthenticationService(RoleRepository roleRepository,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authManager,
                                 TokenService tokenService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    public AppUser registerUser(String username, String password, String email, String name) {
        if (username == null || username.isEmpty()) {
            throw new InvalidInputException("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            throw new InvalidInputException("Password cannot be empty");
        }

        if (password.length() < 8) {
            throw new InvalidInputException("Password must be at least 8 characters");
        }

        if (email == null || email.isEmpty()) {
            throw new InvalidInputException("Email cannot be empty");
        }

        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Name cannot be empty");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new InvalidInputException("Username is already taken");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidInputException("Email is already taken");
        }

        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").orElseThrow();
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);

        // Convert the first character of the name to uppercase
        String formattedName = name.substring(0, 1).toUpperCase() + name.substring(1);

        AppUser user = userRepository.save(
                new AppUser(
                        0,
                        username,
                        encodedPassword,
                        email,
                        formattedName,
                        authorities)
        );

        System.out.println("User " + username + " successfully created");

        return user;
    }

    public Authentication authenticateUser(String username, String password) {
        try {
            Authentication auth =  authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            System.out.println("User " + username + " successfully authenticated");

            return auth;
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException();
        }
    }
}
