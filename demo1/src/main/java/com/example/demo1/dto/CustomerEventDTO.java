package com.example.demo1.dto;

import lombok.Data;

@Data
public class CustomerEventDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String username;
    private String password;
    private String role;  // ✅ maps to manager_id via FK
}
