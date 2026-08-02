package com.insureflow.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PolicyTypeRequest {
    private String name;
    private String description;
    private Integer durationInMonths;
    private BigDecimal basePremium;
}