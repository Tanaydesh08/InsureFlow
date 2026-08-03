package com.insureflow.service;

import com.insureflow.dto.PolicyTypeRequest;
import com.insureflow.dto.PolicyTypeResponse;
import com.insureflow.entity.PolicyType;
import com.insureflow.exception.ResourceNotFoundException;
import com.insureflow.repository.PolicyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyTypeServiceImpl implements PolicyTypeService {

    private final PolicyTypeRepository policyTypeRepository;

    @Override
    public PolicyTypeResponse create(PolicyTypeRequest request) {

        if (policyTypeRepository.existsByName(request.getName())) {
            throw new ResourceNotFoundException("Policy Type already exists");
        }

        PolicyType policyType = PolicyType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .coverageAmount(request.getCoverageAmount())
                .premiumAmount(request.getPremiumAmount())
                .durationInMonths(request.getDurationInMonths())
                .active(true)
                .build();

        policyTypeRepository.save(policyType);

        return mapToResponse(policyType);
    }

    @Override
    public List<PolicyTypeResponse> getAll() {

        return policyTypeRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PolicyTypeResponse getById(Long id) {

        PolicyType policyType = policyTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy Type not found"));

        return mapToResponse(policyType);
    }

    @Override
    public PolicyTypeResponse update(Long id, PolicyTypeRequest request) {

        PolicyType policyType = policyTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy Type not found"));

        policyType.setName(request.getName());
        policyType.setDescription(request.getDescription());
        policyType.setCoverageAmount(request.getCoverageAmount());
        policyType.setPremiumAmount(request.getPremiumAmount());
        policyType.setDurationInMonths(request.getDurationInMonths());

        policyTypeRepository.save(policyType);

        return mapToResponse(policyType);
    }

    @Override
    public void delete(Long id) {

        PolicyType policyType = policyTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy Type not found"));

        policyType.setActive(false);

        policyTypeRepository.save(policyType);
    }

    private PolicyTypeResponse mapToResponse(PolicyType policyType) {

        return PolicyTypeResponse.builder()
                .id(policyType.getId())
                .name(policyType.getName())
                .description(policyType.getDescription())
                .coverageAmount(policyType.getCoverageAmount())
                .premiumAmount(policyType.getPremiumAmount())
                .durationInMonths(policyType.getDurationInMonths())
                .active(policyType.getActive())
                .build();
    }
}