package com.insureflow.repository;

import com.insureflow.entity.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyTypeRepository extends JpaRepository<PolicyType, Long> {
    Optional<PolicyType> findByName(String name);
    boolean existsByName(String name);
}