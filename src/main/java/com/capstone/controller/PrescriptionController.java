package com.capstone.controller;

import com.capstone.dto.PrescriptionRequest;
import com.capstone.service.PrescriptionService;
import com.capstone.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final TokenService tokenService;

    public PrescriptionController(PrescriptionService prescriptionService, TokenService tokenService) {
        this.prescriptionService = prescriptionService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> createPrescription(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody PrescriptionRequest request
    ) {
        // Validate token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return error("Missing or invalid token", HttpStatus.UNAUTHORIZED);
        }

        String token = authorization.substring("Bearer ".length());
        if (!tokenService.isTokenValid(token)) {
            return error("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // Save prescription
        Long prescriptionId = prescriptionService.createPrescription(request);

        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("message", "Prescription created successfully");
        body.put("prescriptionId", prescriptionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    private ResponseEntity<Map<String, Object>> error(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
