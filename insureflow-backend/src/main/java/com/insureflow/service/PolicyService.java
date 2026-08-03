package com.insureflow.service;

import com.insureflow.dto.PolicyRequest;
import com.insureflow.dto.PolicyResponse;

import java.util.List;

public interface PolicyService {

    PolicyResponse purchasePolicy(PolicyRequest request);

    List<PolicyResponse> getAllPolicies();

    List<PolicyResponse> getMyPolicies();

    PolicyResponse getPolicy(Long id);

    void cancelPolicy(Long id);
}