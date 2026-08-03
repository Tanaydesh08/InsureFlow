package com.insureflow.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PolicyTypeResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal coverageAmount;
    private BigDecimal premiumAmount;
    private Integer durationInMonths;
    private Boolean active;
}