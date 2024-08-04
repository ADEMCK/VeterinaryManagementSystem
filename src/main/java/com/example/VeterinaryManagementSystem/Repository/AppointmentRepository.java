package com.example.VeterinaryManagementSystem.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.VeterinaryManagementSystem.Entity.Appointment;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByAppointmentDateAndDoctorIdAndAnimalId(LocalDateTime date, Long id, Long id1);

    Optional<Appointment> findByAppointmentDateAndDoctorId(LocalDateTime date, Long id);

    Page<Appointment> findByAppointmentDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Appointment> findByDoctorIdAndAppointmentDateBetween(Long doctorId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Appointment> findByAnimalIdAndAppointmentDateBetween(Long animalId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}