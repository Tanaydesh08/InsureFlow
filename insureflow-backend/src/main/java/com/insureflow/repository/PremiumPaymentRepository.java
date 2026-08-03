package com.insureflow.repository;

import com.insureflow.entity.Policy;
import com.insureflow.entity.PremiumPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PremiumPaymentRepository extends JpaRepository<PremiumPayment, Long> {

    List<PremiumPayment> findByPolicy(Policy policy);
}