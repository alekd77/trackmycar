package com.trackmycar.trackmycarbackend.service;

import com.trackmycar.trackmycarbackend.dto.LoginResponseDTO;
import com.trackmycar.trackmycarbackend.entity.AppUser;
import com.trackmycar.trackmycarbackend.entity.Role;
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
        String encodedPassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").orElseThrow();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        return userRepository.save(
                new AppUser(
                        0,
                        username,
                        encodedPassword,
                        email,
                        name,
                        authorities)
        );
    }

    public LoginResponseDTO loginUser(String username, String password) {

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(userRepository.findByUsername(username).orElseThrow(), token);

        } catch (AuthenticationException e) {
            return new LoginResponseDTO(null, "");
        }
    }
}
