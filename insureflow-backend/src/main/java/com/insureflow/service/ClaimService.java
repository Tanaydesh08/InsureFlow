package com.insureflow.service;

import com.insureflow.dto.ClaimRequest;
import com.insureflow.dto.ClaimResponse;

import java.util.List;

public interface ClaimService {

    ClaimResponse createClaim(ClaimRequest request);

    List<ClaimResponse> getAllClaims();

    List<ClaimResponse> getMyClaims();

    ClaimResponse getClaim(Long id);

    ClaimResponse updateStatus(Long id, String status);

}