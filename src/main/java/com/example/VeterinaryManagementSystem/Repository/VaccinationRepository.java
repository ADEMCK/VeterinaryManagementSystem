package com.example.VeterinaryManagementSystem.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.VeterinaryManagementSystem.Entity.Vaccination;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    List<Vaccination> findByNameAndCodeAndAnimalIdAndProtectionFinishDateGreaterThanEqual(String name, String code, Long id, LocalDate protectionStartDate);

    Page<Vaccination> findByAnimalId(Long id, Pageable pageable);

    Page<Vaccination> findByProtectionFinishDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<Vaccination> findByAnimalId(Long animalId);

}