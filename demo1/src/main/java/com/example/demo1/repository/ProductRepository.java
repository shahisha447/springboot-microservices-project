package com.example.demo1.repository;

import com.example.demo1.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ✅ Fix — add findByUsername
    Optional<Product> findByUsername(String username);

    // ✅ Search by name OR phone OR address
    Page<Product> findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String name,
            String phone,
            String address,
            Pageable pageable
    );

    boolean existsByUsername(String username);
}