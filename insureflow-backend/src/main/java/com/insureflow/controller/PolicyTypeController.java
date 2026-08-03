package com.insureflow.controller;

import com.insureflow.dto.PolicyTypeRequest;
import com.insureflow.dto.PolicyTypeResponse;
import com.insureflow.service.PolicyTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policy-types")
@RequiredArgsConstructor
public class PolicyTypeController {

    private final PolicyTypeService policyTypeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyTypeResponse> create(
            @Valid @RequestBody PolicyTypeRequest request) {

        return new ResponseEntity<>(
                policyTypeService.create(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<PolicyTypeResponse>> getAll() {

        return ResponseEntity.ok(policyTypeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyTypeResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(policyTypeService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PolicyTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PolicyTypeRequest request) {

        return ResponseEntity.ok(policyTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        policyTypeService.delete(id);

        return ResponseEntity.ok("Policy Type deleted successfully");
    }
}