package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod;    // CASH, CARD, UPI, NETBANKING// PENDING, SUCCESS, FAILED
    private Double amount;
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;             // linked to order

    private String paidBy;           // username who paid
    private LocalDateTime paidAt;    // when paid

    @PrePersist
    public void prePersist() {
        this.paidAt = LocalDateTime.now();
    }
}