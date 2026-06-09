package com.example.demo1.kafka;

import lombok.Data;

@Data
public class UserEvent {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
}