package com.example.demo1.repository;

import com.example.demo1.entity.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {
    boolean existsByName(String name);
}