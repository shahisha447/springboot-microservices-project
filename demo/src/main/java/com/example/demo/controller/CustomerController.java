package com.example.demo.controller;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/api/customers")  // ✅ protected — needs ROLE_CUSTOMER token
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ✅ Needs ROLE_CUSTOMER token
    @Operation(
            summary = "Get All Customers",
            description = "Fetches all customer records from database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // ✅ Needs ROLE_CUSTOMER token
    @Operation(
            summary = "Get Customer By ID",
            description = "Fetch customer details using customer ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // ✅ Needs ROLE_CUSTOMER token
    @Operation(
            summary = "Search Customers",
            description = "Searches customers using keyword with pagination support"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/search")
    public Page<Customer> searchCustomers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return customerService.searchCustomers(keyword, page, size);
    }

    // ✅ Needs ROLE_CUSTOMER token
    @Operation(
            summary = "Update Customer",
            description = "Updates existing customer information"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PutMapping("/{id}")
    public Customer updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer updated) {
        return customerService.updateCustomer(id, updated);
    }

    // ✅ Needs ROLE_CUSTOMER token
    @Operation(
            summary = "Delete Customer",
            description = "Deletes customer using customer ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "Customer deleted successfully!";
    }
    @Operation(
            summary = "Customer Pagination",
            description = "Fetches customers page by page using pagination"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagination data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/pagination")
    public Page<Customer> getCustomers(Pageable pageable){

        return customerService.getCustomers(pageable);
    }
}