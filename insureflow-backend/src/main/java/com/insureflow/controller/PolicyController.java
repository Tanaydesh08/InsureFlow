package com.insureflow.controller;

import com.insureflow.dto.PolicyRequest;
import com.insureflow.dto.PolicyResponse;
import com.insureflow.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PolicyResponse> purchase(
            @Valid @RequestBody PolicyRequest request) {

        return new ResponseEntity<>(
                policyService.purchasePolicy(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PolicyResponse>> getAll() {

        return ResponseEntity.ok(
                policyService.getAllPolicies()
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PolicyResponse>> getMine() {

        return ResponseEntity.ok(
                policyService.getMyPolicies()
        );
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<PolicyResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                policyService.getPolicy(id)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> cancel(
            @PathVariable Long id) {

        policyService.cancelPolicy(id);

        return ResponseEntity.ok("Policy cancelled successfully.");
    }
}