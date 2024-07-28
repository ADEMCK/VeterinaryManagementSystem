package com.example.VeterinaryManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.VeterinaryManagementSystem.Entity")
public class VeterinaryManagementSystemApp {

    public static void main(String[] args) {
        SpringApplication.run(VeterinaryManagementSystemApp.class, args);
    }
}