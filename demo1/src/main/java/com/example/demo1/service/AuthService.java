package com.example.demo1.service;

import com.example.demo1.config.JwtUtil;
import com.example.demo1.dto.AuthResponse;
import com.example.demo1.dto.RegisterRequest;
import com.example.demo1.entity.Manager;
import com.example.demo1.entity.Product;
import com.example.demo1.repository.ManagerRepository;
import com.example.demo1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private ManagerRepository managerRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private AuditLogService auditLogService; // ✅ ADD

    // ✅ Register Manager
    public String registerManager(RegisterRequest request) {
        Manager manager = new Manager();
        manager.setUsername(request.getUsername());
        manager.setPassword(passwordEncoder.encode(request.getPassword()));
        manager.setEmail(request.getEmail());
        manager.setRole("ROLE_MANAGER");
        managerRepository.save(manager);

        // ✅ Log CREATE
        auditLogService.log(
                "MANAGER",
                manager.getUsername(),
                "CREATE",
                "manager",
                "Manager registered with email: " + manager.getEmail()
        );

        return "Manager registered successfully!";
    }

    // ✅ Register Product
    public String registerProduct(RegisterRequest request) {
        Product product = new Product();
        product.setUsername(request.getUsername());
        product.setPassword(passwordEncoder.encode(request.getPassword()));
        product.setName(request.getName());
        product.setPhone(request.getPhone());
        product.setAddress(request.getAddress());
        product.setRole("ROLE_PRODUCT");
        productRepository.save(product);

        // ✅ Log CREATE
        auditLogService.log(
                "PRODUCT",
                product.getUsername(),
                "CREATE",
                "product",
                "Product registered with name: " + product.getName()
        );

        return "Product registered successfully!";
    }

    // ✅ Login
    public AuthResponse login(String username, String password) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        AuthResponse response = new AuthResponse();

        // ✅ Check Manager table first
        var managerOpt = managerRepository.findByUsername(username);
        if (managerOpt.isPresent()) {
            Manager manager = managerOpt.get();
            String token = jwtUtil.generateToken(
                    username, manager.getRole());

            response.setToken(token);
            response.setId(manager.getId());
            response.setUsername(manager.getUsername());
            response.setEmail(manager.getEmail());
            response.setRole(manager.getRole());

            // ✅ Log LOGIN
            auditLogService.log(
                    "MANAGER",
                    username,
                    "LOGIN",
                    "manager",
                    "Manager logged in: " + username
            );

            return response;
        }

        // ✅ Check Product table
        var productOpt = productRepository.findByUsername(username);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            String token = jwtUtil.generateToken(
                    username, product.getRole());

            response.setToken(token);
            response.setId(product.getId());
            response.setUsername(product.getUsername());
            response.setEmail(null);
            response.setRole(product.getRole());

            // ✅ Log LOGIN
            auditLogService.log(
                    "PRODUCT",
                    username,
                    "LOGIN",
                    "product",
                    "Product logged in: " + username
            );

            return response;
        }

        throw new RuntimeException("User not found!");
    }
}