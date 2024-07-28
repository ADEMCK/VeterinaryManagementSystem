package com.example.VeterinaryManagementSystem.Exception;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException(Class entityClass) {
        super("The " + entityClass.getSimpleName() + " has already been saved.");
    }
}