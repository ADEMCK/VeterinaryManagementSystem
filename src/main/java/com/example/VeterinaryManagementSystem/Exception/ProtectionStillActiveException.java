package com.example.VeterinaryManagementSystem.Exception;

public class ProtectionStillActiveException extends RuntimeException {
    public ProtectionStillActiveException(String message) {
        super(message);
    }
}