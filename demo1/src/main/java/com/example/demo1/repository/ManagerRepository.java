package com.example.demo1.repository;

import com.example.demo1.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    // ✅ Fix — add findByUsername
    Optional<Manager> findByUsername(String username);

    // ✅ Search by username OR email
    Page<Manager> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String username,
            String email,
            Pageable pageable
    );

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}