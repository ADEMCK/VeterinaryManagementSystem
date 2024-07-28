package com.example.VeterinaryManagementSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.VeterinaryManagementSystem.Entity.Report;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByAppointmentId(Long appointmentId);

    Optional<Report> findReportByTitleAndDiagnosisAndPriceAndAppointmentId(String title, String diagnosis, double price, Long appointment_id);
}