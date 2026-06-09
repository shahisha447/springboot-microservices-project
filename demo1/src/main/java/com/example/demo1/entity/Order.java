package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Only 2 FKs
    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnoreProperties({"password", "role"})
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"password", "role", "username"})
    private Product product;

    // ✅ Who created
    private String createdBy;

    // ✅ Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}