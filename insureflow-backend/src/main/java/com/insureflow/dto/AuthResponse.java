package com.insureflow.dto;

import com.insureflow.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type;
    private String message;
    private Role role;
    private String fullName;
}