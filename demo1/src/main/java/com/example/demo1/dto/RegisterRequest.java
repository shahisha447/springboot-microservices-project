package com.example.demo1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // optional - only for Manager
    private String email;

    // optional - only for Product
    private String name;
    private String phone;
    private String address;
}