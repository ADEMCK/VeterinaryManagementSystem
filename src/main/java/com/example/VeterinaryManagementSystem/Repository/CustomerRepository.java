package com.example.VeterinaryManagementSystem.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.VeterinaryManagementSystem.Entity.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByNameAndEmail(String name, String email);

    Page<Customer> findByNameContaining(String name, Pageable pageable);
}
