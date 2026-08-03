package com.insureflow.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PolicyResponse {

    private Long id;

    private String policyNumber;

    private String policyType;

    private BigDecimal premiumAmount;

    private BigDecimal coverageAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;
}