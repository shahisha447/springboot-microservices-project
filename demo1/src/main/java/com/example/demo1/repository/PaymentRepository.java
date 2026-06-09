package com.example.demo1.repository;

import com.example.demo1.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPaidBy(String paidBy);
    List<Payment> findByOrderId(Long orderId);

    @Query("SELECT p FROM Payment p WHERE " +
            "LOWER(p.paymentMethod) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(p.paidBy) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    Page<Payment> search(String keyword, Pageable pageable);

    @Query("SELECT SUM(p.amount) FROM Payment p")
    Double sumAllPayments();

    List<Payment> findTop5ByOrderByIdDesc();
}