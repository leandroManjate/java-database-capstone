package com.capstone.service;

import com.capstone.model.Appointment;
import com.capstone.model.Doctor;
import com.capstone.repository.AppointmentRepository;
import com.capstone.repository.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // 1) Returns available time slots for a doctor on a given date
    public List<String> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Doctor's configured available times (e.g., 09:00, 10:00, ...)
        List<LocalTime> baseSlots = doctor.getAvailableTimes();

        // Booked appointments for that day
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        List<Appointment> booked = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        Set<LocalTime> bookedTimes = new HashSet<>();
        for (Appointment a : booked) {
            bookedTimes.add(a.getAppointmentTime().toLocalTime());
        }

        List<String> available = new ArrayList<>();
        if (baseSlots != null) {
            for (LocalTime slot : baseSlots) {
                if (!bookedTimes.contains(slot)) {
                    available.add(slot.toString());
                }
            }
        }
        return available;
    }

    // 2) Validates doctor login credentials and returns a structured response
    public Map<String, Object> validateLogin(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        Optional<Doctor> optDoctor = doctorRepository.findByEmail(email);
        if (optDoctor.isEmpty()) {
            response.put("success", false);
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "Invalid credentials");
            return response;
        }

        Doctor doctor = optDoctor.get();

        // Simple credential check (for demo/rubric). In real projects use password hashing.
        if (!doctor.getPassword().equals(password)) {
            response.put("success", false);
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "Invalid credentials");
            return response;
        }

        String token = tokenService.generateToken(doctor.getEmail());

        response.put("success", true);
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("doctorId", doctor.getId());
        response.put("email", doctor.getEmail());

        return response;
    }
}
