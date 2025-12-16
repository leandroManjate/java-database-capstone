package com.capstone.repository;

import com.capstone.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // 1) Retrieve patient by email
    Optional<Patient> findByEmail(String email);

    // 2) Retrieve patient by either email or phone number
    Optional<Patient> findByEmailOrPhone(String email, String phone);
}
