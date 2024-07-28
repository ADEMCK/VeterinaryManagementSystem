package com.example.VeterinaryManagementSystem.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.VeterinaryManagementSystem.Entity.Animal;
import com.example.VeterinaryManagementSystem.Entity.Doctor;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private Long id;
    private LocalDateTime appointmentDate;
    private Doctor doctor;
    private Animal animal;
}