package com.example.demo1.controller;

import com.example.demo1.dto.AuthResponse;
import com.example.demo1.dto.LoginRequest;
import com.example.demo1.dto.RegisterRequest;
import com.example.demo1.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthService authService;

    // ✅ Register as Manager (Group 1)
    @Operation(
            summary = "Register Manager",
            description = "Registers a new manager account in the system"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Manager registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping("/register/manager")
    public String registerManager(@Valid @RequestBody RegisterRequest request) {
        return authService.registerManager(request);
    }

    // ✅ Register as Product (Group 2)
    @Operation(
            summary = "Register Product User",
            description = "Registers a new product user account in the system"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product user registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping("/register/product")
    public String registerProduct(@Valid @RequestBody RegisterRequest request) {
        return authService.registerProduct(request);
    }

    // ✅ Login - same endpoint for both
    @Operation(
            summary = "User Login",
            description = "Authenticates user and returns JWT token"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }
}