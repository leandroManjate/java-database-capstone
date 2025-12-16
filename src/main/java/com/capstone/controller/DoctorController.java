package com.capstone.controller;

import com.capstone.service.DoctorService;
import com.capstone.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final TokenService tokenService;

    public DoctorController(DoctorService doctorService, TokenService tokenService) {
        this.doctorService = doctorService;
        this.tokenService = tokenService;
    }

    // GET /api/doctors/{doctorId}/availability?date=2025-12-16
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<?> getDoctorAvailability(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Long doctorId,
            @RequestParam String date
    ) {
        // 1) Validate token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return error("Missing or invalid token", HttpStatus.UNAUTHORIZED);
        }

        String token = authorization.substring("Bearer ".length());
        if (!tokenService.isTokenValid(token)) {
            return error("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // 2) Use dynamic parameters (doctorId + date)
        LocalDate requestedDate = LocalDate.parse(date);
        List<String> availableSlots = doctorService.getAvailableTimeSlots(doctorId, requestedDate);

        Map<String, Object> body = new HashMap<>();
        body.put("success", true);
        body.put("doctorId", doctorId);
        body.put("date", requestedDate.toString());
        body.put("availableSlots", availableSlots);

        return ResponseEntity.ok(body);
    }

    private ResponseEntity<Map<String, Object>> error(String message, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
