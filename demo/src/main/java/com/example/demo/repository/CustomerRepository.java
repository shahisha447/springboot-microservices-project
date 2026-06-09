package com.example.demo.repository;

import com.example.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUsername(String username);

    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email); // ✅ ADD THIS
}