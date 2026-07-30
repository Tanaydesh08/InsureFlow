package com.insureflow.service;

import com.insureflow.dto.AuthResponse;
import com.insureflow.dto.LoginRequest;
import com.insureflow.dto.RegisterRequest;
import com.insureflow.entity.User;
import com.insureflow.enums.Role;
import com.insureflow.repository.UserRepository;
import com.insureflow.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements AuthenticationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists..!!");
        }
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(user);

        String token = jwtService.generateToken(new com.insureflow.security.CustomUserDetails(user));

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .message("Registration Successful")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(new com.insureflow.security.CustomUserDetails(user));

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .message("Login Successful")
                .build();
    }
}