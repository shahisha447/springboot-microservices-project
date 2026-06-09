package com.example.demo1.controller;

import com.example.demo1.entity.Order;
import com.example.demo1.entity.Payment;
import com.example.demo1.entity.Report;

import com.example.demo1.repository.CustomerInfoRepository;
import com.example.demo1.repository.ManagerRepository;
import com.example.demo1.repository.OrderRepository;
import com.example.demo1.repository.PaymentRepository;
import com.example.demo1.repository.ProductRepository;
import com.example.demo1.repository.ReportRepository;
import com.example.demo1.repository.UserInfoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CustomerInfoRepository customerInfoRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ProductRepository productRepository;

    // ✅ Dashboard API
    @Operation(
            summary = "Dashboard Analytics",
            description = "Provides dashboard statistics including total users, customers, orders, payments, reports, revenue, latest orders, latest payments, latest reports, managers, and products"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping
    public Map<String, Object> getDashboard() {

        Map<String, Object> dashboard =
                new HashMap<>();

        // ✅ Total Counts
        dashboard.put(
                "totalUsers",
                userInfoRepository.count());

        dashboard.put(
                "totalCustomers",
                customerInfoRepository.count());

        dashboard.put(
                "totalOrders",
                orderRepository.count());

        dashboard.put(
                "totalPayments",
                paymentRepository.count());

        dashboard.put(
                "totalReports",
                reportRepository.count());

        dashboard.put(
                "totalManagers",
                managerRepository.count());

        dashboard.put(
                "totalProducts",
                productRepository.count());

        // ✅ Total Revenue
        Double revenue =
                paymentRepository.sumAllPayments();

        dashboard.put(
                "totalRevenue",
                revenue != null ? revenue : 0);

        // ✅ Latest Orders
        List<Order> latestOrders =
                orderRepository.findTop5ByOrderByIdDesc();

        dashboard.put(
                "latestOrders",
                latestOrders);

        // ✅ Latest Payments
        List<Payment> latestPayments =
                paymentRepository.findTop5ByOrderByIdDesc();

        dashboard.put(
                "latestPayments",
                latestPayments);

        // ✅ Latest Reports
        List<Report> latestReports =
                reportRepository.findTop5ByOrderByIdDesc();

        dashboard.put(
                "latestReports",
                latestReports);

        return dashboard;
    }
}