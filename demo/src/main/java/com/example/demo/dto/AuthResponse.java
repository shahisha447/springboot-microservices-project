package com.example.demo.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long id;
    private String username;
    private String email;
    private String role;
    // ✅ No customers here anymore
}