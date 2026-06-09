package com.example.demo.controller;

import com.example.demo.dto.CustomerAuthResponse;
import com.example.demo.dto.CustomerRegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.service.CustomerAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/customer")  // ✅ public auth only
public class CustomerAuthController {

    @Autowired
    private CustomerAuthService customerAuthService;

    // ✅ Public — no token needed
    @Operation(
            summary = "Customer Registration",
            description = "Registers a new customer account"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping("/register")
    public String register(
            @Valid @RequestBody CustomerRegisterRequest request) {
        return customerAuthService.register(request);
    }

    // ✅ Public — no token needed
    @Operation(
            summary = "Customer Login",
            description = "Authenticates customer and returns JWT token"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public CustomerAuthResponse login(
            @Valid @RequestBody LoginRequest request) {
        return customerAuthService.login(
                request.getUsername(),
                request.getPassword()
        );
    }
}