package com.insureflow.service;

import com.insureflow.dto.AuthResponse;
import com.insureflow.dto.LoginRequest;
import com.insureflow.dto.RegisterRequest;
import com.insureflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest registerRequest){
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest){
        return null;
    }
}
