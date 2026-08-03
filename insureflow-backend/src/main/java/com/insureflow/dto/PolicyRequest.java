package com.insureflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PolicyRequest {

    @NotNull(message = "Policy Type is required")
    private Long policyTypeId;
}