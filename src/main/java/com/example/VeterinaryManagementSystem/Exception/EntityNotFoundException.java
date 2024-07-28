package com.example.VeterinaryManagementSystem.Exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Long id, Class<?> entityClass) {
        super(entityClass.getSimpleName() + " with id: " + id + " not found.");
    }
}
