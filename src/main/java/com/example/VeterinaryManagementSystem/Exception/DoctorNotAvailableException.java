package com.example.VeterinaryManagementSystem.Exception;

import java.time.LocalDate;

public class DoctorNotAvailableException extends RuntimeException {
    public DoctorNotAvailableException(LocalDate date) {
        super("The Doctor doesn't work on " + date);
    }
}