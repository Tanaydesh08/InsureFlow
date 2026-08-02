package com.insureflow.service;

import com.insureflow.dto.PolicyTypeRequest;
import com.insureflow.dto.PolicyTypeResponse;

import java.util.List;

public interface PolicyTypeService {
    PolicyTypeResponse create(PolicyTypeRequest request);
    PolicyTypeResponse getById(Long id);

    List<PolicyTypeResponse> getAll();
    PolicyTypeResponse update(Long id, PolicyTypeRequest request);

    void delete(Long id);
}