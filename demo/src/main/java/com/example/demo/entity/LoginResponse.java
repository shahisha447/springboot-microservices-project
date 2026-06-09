package com.example.demo.entity;

import java.util.List;

public class LoginResponse {

    private String token;
    private User user;
    private List<Customer> customers;

    public LoginResponse(String token, User user, List<Customer> customers, List<User> allUsers) {
        this.token = token;
        this.user = user;
        this.customers = customers;
    }

    public String getToken() { return token; }
    public User getUser() { return user; }
    public List<Customer> getCustomers() { return customers; }
}