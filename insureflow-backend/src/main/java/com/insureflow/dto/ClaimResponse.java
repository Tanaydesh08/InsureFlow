package com.insureflow.dto;

import com.insureflow.enums.ClaimStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ClaimResponse {

    private Long id;

    private String policyNumber;

    private BigDecimal claimAmount;

    private String reason;

    private String description;

    private ClaimStatus status;

    private LocalDateTime claimDate;

}