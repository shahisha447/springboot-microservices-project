package com.example.demo1.dto;

import lombok.Data;

@Data
public class UserEventDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;
}