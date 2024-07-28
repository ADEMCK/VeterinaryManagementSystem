package com.example.VeterinaryManagementSystem.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.VeterinaryManagementSystem.Entity.Doctor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkDayResponse {

    private Long id;
    private LocalDate workDay;
    private Doctor doctor;
}