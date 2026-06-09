package com.example.demo.kafka;

import lombok.Data;

@Data
public class CustomerEvent {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private Long userId;
}