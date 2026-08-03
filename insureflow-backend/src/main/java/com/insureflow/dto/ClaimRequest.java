package com.insureflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClaimRequest {

    @NotNull(message = "Policy Id is required")
    private Long policyId;

    @NotNull(message = "Claim amount is required")
    @Positive(message = "Claim amount must be greater than zero")
    private BigDecimal claimAmount;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotBlank(message = "Description is required")
    private String description;
}