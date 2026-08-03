package com.insureflow.service;

import com.insureflow.dto.PaymentRequest;
import com.insureflow.dto.PaymentResponse;
import com.insureflow.entity.Policy;
import com.insureflow.entity.PremiumPayment;
import com.insureflow.entity.User;
import com.insureflow.enums.PaymentStatus;
import com.insureflow.exception.ResourceNotFoundException;
import com.insureflow.repository.PolicyRepository;
import com.insureflow.repository.PremiumPaymentRepository;
import com.insureflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PremiumPaymentRepository paymentRepository;
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    @Override
    public PaymentResponse makePayment(PaymentRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (!policy.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("You are not authorized to pay for this policy.");
        }

        PremiumPayment payment = PremiumPayment.builder()
                .policy(policy)
                .amount(policy.getPremiumAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.SUCCESS)
                .transactionId(generateTransactionId())
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return map(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<PaymentResponse> getMyPayments() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return paymentRepository.findAll()
                .stream()
                .filter(payment ->
                        payment.getPolicy().getUser().getId().equals(user.getId()))
                .map(this::map)
                .toList();
    }

    @Override
    public PaymentResponse getPayment(Long id) {

        PremiumPayment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return map(payment);
    }

    private String generateTransactionId() {

        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 10)
                        .toUpperCase();
    }

    private PaymentResponse map(PremiumPayment payment) {

        return PaymentResponse.builder()
                .id(payment.getId())
                .policyNumber(payment.getPolicy().getPolicyNumber())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}