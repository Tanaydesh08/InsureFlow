package com.insureflow.entity;

import com.insureflow.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String  password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean enable = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}