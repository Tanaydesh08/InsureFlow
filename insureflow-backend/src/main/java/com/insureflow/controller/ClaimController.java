package com.insureflow.controller;

import com.insureflow.dto.ClaimRequest;
import com.insureflow.dto.ClaimResponse;
import com.insureflow.service.ClaimService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    // CUSTOMER can raise a claim
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ClaimResponse> createClaim(
            @Valid @RequestBody ClaimRequest request) {

        return new ResponseEntity<>(
                claimService.createClaim(request),
                HttpStatus.CREATED
        );
    }

    // ADMIN can view all claims
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {

        return ResponseEntity.ok(
                claimService.getAllClaims()
        );
    }

    // CUSTOMER can view only their own claims
    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<ClaimResponse>> getMyClaims() {

        return ResponseEntity.ok(
                claimService.getMyClaims()
        );
    }

    // ADMIN or CUSTOMER can view a claim by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ClaimResponse> getClaim(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                claimService.getClaim(id)
        );
    }

    // ADMIN can approve/reject claims
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClaimResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                claimService.updateStatus(id, status)
        );
    }
}