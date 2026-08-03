package com.insureflow.mapper;

import com.insureflow.entity.User;
import com.insureflow.enums.Role;
import com.insureflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@insureflow.com").isEmpty()) {

            User admin = User.builder()
                    .fullName("System Admin")
                    .email("admin@insureflow.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .enable(true)
                    .build();

            userRepository.save(admin);
        }
    }
}