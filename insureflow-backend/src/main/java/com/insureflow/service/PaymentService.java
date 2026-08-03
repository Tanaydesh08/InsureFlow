package com.insureflow.service;

import com.insureflow.dto.PaymentRequest;
import com.insureflow.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest request);

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getMyPayments();

    PaymentResponse getPayment(Long id);
}