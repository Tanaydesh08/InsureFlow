package com.insureflow.repository;

import com.insureflow.entity.Claim;
import com.insureflow.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByPolicy(Policy policy);

}