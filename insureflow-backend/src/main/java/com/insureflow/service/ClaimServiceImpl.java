package com.insureflow.service;

import com.insureflow.dto.ClaimRequest;
import com.insureflow.dto.ClaimResponse;
import com.insureflow.entity.Claim;
import com.insureflow.entity.Policy;
import com.insureflow.entity.User;
import com.insureflow.enums.ClaimStatus;
import com.insureflow.exception.ResourceNotFoundException;
import com.insureflow.repository.ClaimRepository;
import com.insureflow.repository.PolicyRepository;
import com.insureflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    @Override
    public ClaimResponse createClaim(ClaimRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        if (!policy.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("You are not allowed to raise a claim for this policy.");
        }

        Claim claim = Claim.builder()
                .policy(policy)
                .claimAmount(request.getClaimAmount())
                .reason(request.getReason())
                .description(request.getDescription())
                .status(ClaimStatus.PENDING)
                .claimDate(LocalDateTime.now())
                .build();

        claimRepository.save(claim);

        return map(claim);
    }

    @Override
    public List<ClaimResponse> getAllClaims() {

        return claimRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<ClaimResponse> getMyClaims() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return claimRepository.findAll()
                .stream()
                .filter(claim ->
                        claim.getPolicy().getUser().getId().equals(user.getId()))
                .map(this::map)
                .toList();
    }

    @Override
    public ClaimResponse getClaim(Long id) {

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        return map(claim);
    }

    @Override
    public ClaimResponse updateStatus(Long id, String status) {

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        claim.setStatus(ClaimStatus.valueOf(status.toUpperCase()));

        claimRepository.save(claim);

        return map(claim);
    }

    private ClaimResponse map(Claim claim) {

        return ClaimResponse.builder()
                .id(claim.getId())
                .policyNumber(claim.getPolicy().getPolicyNumber())
                .claimAmount(claim.getClaimAmount())
                .reason(claim.getReason())
                .description(claim.getDescription())
                .status(claim.getStatus())
                .claimDate(claim.getClaimDate())
                .build();
    }
}