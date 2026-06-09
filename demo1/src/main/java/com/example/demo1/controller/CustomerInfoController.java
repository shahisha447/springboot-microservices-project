package com.example.demo1.controller;

import com.example.demo1.entity.CustomerInfo;
import com.example.demo1.repository.CustomerInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/customerinfo")
public class CustomerInfoController {

    @Autowired private CustomerInfoRepository customerInfoRepository;

    @Operation(
            summary = "Get All Customer Information",
            description = "Fetches all customer records from database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer records retrieved successfully")
    })
    @GetMapping
    public List<CustomerInfo> getAll() {
        return customerInfoRepository.findAll();
    }

    @Operation(
            summary = "Get Customer Information By ID",
            description = "Fetch customer details using customer ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer information found successfully"),
            @ApiResponse(responseCode = "404", description = "Customer information not found")
    })
    @GetMapping("/{id}")
    public CustomerInfo getById(@PathVariable Long id) {
        return customerInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CustomerInfo not found"));
    }
    @Operation(
            summary = "Update Customer Information",
            description = "Updates existing customer details"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer information updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer information not found")
    })

    @PutMapping("/{id}")
    public CustomerInfo update(
            @PathVariable Long id,
            @RequestBody CustomerInfo updated) {

        CustomerInfo customer = customerInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CustomerInfo not found"));

        customer.setName(updated.getName());
        customer.setEmail(updated.getEmail());
        customer.setPhone(updated.getPhone());
        customer.setAddress(updated.getAddress());
        customer.setUsername(updated.getUsername()); // ✅
        customer.setRole(updated.getRole());
        customer.setPassword(updated.getPassword());// ✅

        return customerInfoRepository.save(customer);
    }

    @Operation(
            summary = "Delete Customer Information",
            description = "Deletes customer record using customer ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer information deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer information not found")
    })
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        customerInfoRepository.deleteById(id);
        return "CustomerInfo deleted!";
    }
}