package com.example.demo.kafka;

import lombok.Data;

@Data
public class UserEvent {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
}