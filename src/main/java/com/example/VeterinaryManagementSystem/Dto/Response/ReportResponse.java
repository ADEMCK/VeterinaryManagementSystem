package com.example.VeterinaryManagementSystem.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private Long id;
    private String title;
    private String diagnosis;
    private double price;
    private AppointmentForReportResponse appointment;

    public void setAppointment(AppointmentForReportResponse appointment) {
        this.appointment = appointment;
    }

    public AppointmentForReportResponse getAppointment() {
        return appointment;
    }
}