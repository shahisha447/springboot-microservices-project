package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.dto.CustomerAuthResponse;
import com.example.demo.dto.CustomerRegisterRequest;
import com.example.demo.entity.Customer;
import com.example.demo.kafka.KafkaProducer;
import com.example.demo.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerAuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Qualifier("customerAuthenticationManager")
    private AuthenticationManager authManager;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KafkaProducer kafkaProducer;

    // LOGIN
    public CustomerAuthResponse login(String username, String password) {

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        String token = jwtUtil.generateToken(username, customer.getRole());

        CustomerAuthResponse response = new CustomerAuthResponse();
        response.setToken(token);
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setUsername(customer.getUsername());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setRole(customer.getRole());

        return response;
    }

    // REGISTER
    // REGISTER
    public String register(CustomerRegisterRequest request) {

        if (customerRepository.existsByUsername(request.getUsername()))
            return "Username already exists!";

        if (customerRepository.existsByEmail(request.getEmail()))
            return "Email already exists!";

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setUsername(request.getUsername());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setRole("ROLE_CUSTOMER");

        customerRepository.save(customer);

        // ✅ Non-blocking Kafka send
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("action", "CREATE");
            data.put("id", customer.getId());
            data.put("name", customer.getName());
            data.put("username", customer.getUsername());
            data.put("email", customer.getEmail());
            data.put("phone", customer.getPhone());
            data.put("address", customer.getAddress());
            data.put("role", customer.getRole());
            data.put("password", customer.getPassword());
            kafkaProducer.sendCustomer(data);
        } catch (Exception e) {
            System.out.println("⚠️ Kafka not available: " + e.getMessage());
        }

        return "Customer registered successfully!";
    }
}