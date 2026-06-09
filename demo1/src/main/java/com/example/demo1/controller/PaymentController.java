package com.example.demo1.controller;

import com.example.demo1.entity.Payment;
import com.example.demo1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired private PaymentService paymentService;

    // ✅ Create
    @Operation(
            summary = "Create Payment",
            description = "Creates payment for a specific order"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment data")
    })
    @PostMapping("/order/{orderId}")
    public Payment create(@PathVariable Long orderId,
                          @RequestBody Payment payment) {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        return paymentService.createPayment(orderId, payment, auth.getName());
    }

    // ✅ Get All
    @Operation(
            summary = "Get All Payments",
            description = "Fetches all payment records from database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    })
    @GetMapping
    public List<Payment> getAll() {
        return paymentService.getAll();
    }

    // ✅ Get by ID
    @Operation(
            summary = "Get Payment By ID",
            description = "Fetch payment details using payment ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment found successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @GetMapping("/{id}")
    public Payment getById(@PathVariable Long id) {
        return paymentService.getById(id);
    }

    // ✅ Get by Order
    @Operation(
            summary = "Get Payments By Order",
            description = "Fetches all payments related to a specific order"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/order/{orderId}")
    public List<Payment> getByOrder(@PathVariable Long orderId) {
        return paymentService.getByOrder(orderId);
    }

    // ✅ Get by PaidBy
    @Operation(
            summary = "Get Payments By Paid User",
            description = "Fetches payments using payer username"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/paidby/{paidBy}")
    public List<Payment> getByPaidBy(@PathVariable String paidBy) {
        return paymentService.getByPaidBy(paidBy);
    }

    // ✅ Delete
    @Operation(
            summary = "Delete Payment",
            description = "Deletes payment using payment ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        paymentService.delete(id, auth.getName());
        return "Payment deleted!";
    }

    // ✅ Search
    @Operation(
            summary = "Search Payments",
            description = "Searches payments using keyword with pagination and sorting"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public Page<Payment> search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return paymentService.search(keyword, page, size, sortBy, direction);
    }
}