package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportType;
    private String title;
    private String description;
    private Double totalAmount;
    private Integer totalOrders;
    private Integer totalProducts;
    private String generatedBy;
    private LocalDateTime generatedAt;

    // ← ADD these two
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    @PrePersist
    public void prePersist() {
        this.generatedAt = LocalDateTime.now();
    }
}