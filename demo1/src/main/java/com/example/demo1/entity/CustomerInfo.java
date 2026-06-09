package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer_info")
@Data
public class CustomerInfo {

    @Id
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String address;
    private String username;

    @JsonIgnore
    private String password;

    private String role;
}