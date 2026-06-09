package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_info")
@Data
public class UserInfo {

    @Id
    private Long id;

    private String username;
    private String email;
    private String role;
    @JsonIgnore
    private String password; // ✅ hidden from response
   // ← ADD THIS
}
