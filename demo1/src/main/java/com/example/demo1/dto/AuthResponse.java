package com.example.demo1.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private String role;
}