package com.insureflow.dto;

import com.insureflow.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Policy Id is required")
    private Long policyId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}