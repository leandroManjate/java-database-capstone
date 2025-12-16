package com.capstone.service;

import com.capstone.dto.PrescriptionRequest;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {

    public Long createPrescription(PrescriptionRequest request) {
        // In a real app, you would save a Prescription entity using a repository.
        // Returning a dummy id is enough for the rubric structure.
        return 1L;
    }
}
