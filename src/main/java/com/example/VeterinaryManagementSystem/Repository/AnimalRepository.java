package com.example.VeterinaryManagementSystem.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.VeterinaryManagementSystem.Entity.Animal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByNameAndSpeciesAndGenderAndDateOfBirth(String name, String species, String gender, LocalDate dateOfBirth);

    Page<Animal> findByNameContaining(String name, Pageable pageable);

    Page<Animal> findByCustomerName(String customerName, Pageable pageable);


}