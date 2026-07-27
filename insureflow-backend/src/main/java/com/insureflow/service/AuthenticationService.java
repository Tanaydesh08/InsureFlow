package com.insureflow.service;

import com.insureflow.dto.AuthResponse;
import com.insureflow.dto.LoginRequest;
import com.insureflow.dto.RegisterRequest;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
