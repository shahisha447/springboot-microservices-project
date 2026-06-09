package com.example.demo1.dto;

import lombok.Data;

@Data  // ✅ Lombok generates correct getters/setters automatically
public class LoginRequest {
    private String username;
    private String password;
}