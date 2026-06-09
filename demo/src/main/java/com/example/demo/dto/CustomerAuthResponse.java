package com.example.demo.dto;

import lombok.Data;

@Data
public class CustomerAuthResponse {
    private String token;
    private Long id;
    private String name;
    private String username;
    private String phone;
    private String address;
    private String role;
}