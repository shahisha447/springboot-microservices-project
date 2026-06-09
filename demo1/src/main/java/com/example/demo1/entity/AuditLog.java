package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ WHO did it
    private String userType;   // USER, CUSTOMER, MANAGER, PRODUCT

    private String username;   // who did it

    // ✅ WHAT they did
    private String action;     // CREATE, UPDATE, DELETE

    // ✅ WHERE they did it
    private String tableName;  // users, customers, managers, products, orders

    // ✅ WHAT changed
    @Column(columnDefinition = "TEXT")
    private String description; // details of what changed

    // ✅ WHEN
    private LocalDateTime timestamp;

    @PrePersist
    public void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}