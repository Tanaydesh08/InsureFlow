package com.insureflow.service;

import com.insureflow.dto.PolicyRequest;
import com.insureflow.dto.PolicyResponse;
import com.insureflow.entity.Policy;
import com.insureflow.entity.PolicyType;
import com.insureflow.entity.User;
import com.insureflow.enums.PolicyStatus;
import com.insureflow.exception.ResourceNotFoundException;
import com.insureflow.repository.PolicyRepository;
import com.insureflow.repository.PolicyTypeRepository;
import com.insureflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PolicyTypeRepository policyTypeRepository;
    private final UserRepository userRepository;

    @Override
    public PolicyResponse purchasePolicy(PolicyRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PolicyType policyType = policyTypeRepository
                .findByIdAndActiveTrue(request.getPolicyTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy Type not found"));

        Policy policy = Policy.builder()
                .policyNumber(generatePolicyNumber())
                .user(user)
                .policyType(policyType)
                .premiumAmount(policyType.getPremiumAmount())
                .coverageAmount(policyType.getCoverageAmount())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now()
                        .plusMonths(policyType.getDurationInMonths()))
                .status(PolicyStatus.ACTIVE)
                .build();

        policyRepository.save(policy);

        return map(policy);
    }

    @Override
    public List<PolicyResponse> getAllPolicies() {

        return policyRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<PolicyResponse> getMyPolicies() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return policyRepository.findByUser(user)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public PolicyResponse getPolicy(Long id) {

        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        return map(policy);
    }

    @Override
    public void cancelPolicy(Long id) {

        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        policy.setStatus(PolicyStatus.CANCELLED);

        policyRepository.save(policy);
    }

    private String generatePolicyNumber() {

        return "POL-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

    private PolicyResponse map(Policy policy) {

        return PolicyResponse.builder()
                .id(policy.getId())
                .policyNumber(policy.getPolicyNumber())
                .policyType(policy.getPolicyType().getName())
                .premiumAmount(policy.getPremiumAmount())
                .coverageAmount(policy.getCoverageAmount())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .status(policy.getStatus().name())
                .build();
    }
}