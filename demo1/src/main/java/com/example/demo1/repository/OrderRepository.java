package com.example.demo1.repository;

import com.example.demo1.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ Only manager and product FKs
    List<Order> findByManager_Id(Long managerId);
    List<Order> findByProduct_Id(Long productId);
    List<Order> findTop5ByOrderByIdDesc();

    // ✅ Search
    Page<Order> findByCreatedByContainingIgnoreCase(
            String keyword, Pageable pageable);
}