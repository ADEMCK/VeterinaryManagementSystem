package com.example.VeterinaryManagementSystem.Exception;

import java.time.LocalDate;

public class DoctorAppointmentConflictException extends RuntimeException {
    public DoctorAppointmentConflictException(LocalDate date) {
        super();
    }
}