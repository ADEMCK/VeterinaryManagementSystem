package com.example.VeterinaryManagementSystem.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.VeterinaryManagementSystem.Entity.WorkDay;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {

    Optional<WorkDay> findByWorkDayAndDoctor_Id(LocalDate availableDate, Long id);

    Optional<WorkDay> findByDoctorIdAndWorkDay(Long id, LocalDate availableDate);
}