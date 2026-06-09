package com.example.demo1.controller;

import com.example.demo1.dto.OrderRequest;
import com.example.demo1.entity.Order;
import com.example.demo1.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // ✅ Create Order
    @Operation(
            summary = "Create Order",
            description = "Creates a new order using manager ID and product ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid order data")
    })
    @PostMapping
    public Order createOrder(@RequestBody OrderRequest request) {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        String createdBy = auth.getName(); // ✅ from token

        return orderService.createOrder(
                request.getManagerId(),
                request.getProductId(),
                createdBy
        );
    }

    @Operation(
            summary = "Get All Orders",
            description = "Fetches all orders from database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    })
    @GetMapping
    public List<Order> getAll() {
        return orderService.getAllOrders();
    }

    @Operation(
            summary = "Get Order By ID",
            description = "Fetch order details using order ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @Operation(
            summary = "Delete Order",
            description = "Deletes order using order ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "Order deleted!";
    }

    @Operation(
            summary = "Get Orders By Manager",
            description = "Fetches all orders created by a specific manager"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    // ✅ Get by Manager
    @GetMapping("/manager/{managerId}")
    public List<Order> getByManager(@PathVariable Long managerId) {
        return orderService.getOrdersByManager(managerId);
    }

    @Operation(
            summary = "Get Orders By Product",
            description = "Fetches all orders related to a specific product"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    // ✅ Get by Product
    @GetMapping("/product/{productId}")
    public List<Order> getByProduct(@PathVariable Long productId) {
        return orderService.getOrdersByProduct(productId);
    }

    // ✅ Search + Pagination
    @Operation(
            summary = "Search Orders With Pagination",
            description = "Searches orders using keyword with pagination and sorting support"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public Page<Order> search(
            @RequestParam(defaultValue = "")     String keyword,
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "5")    int size,
            @RequestParam(defaultValue = "id")   String sortBy,
            @RequestParam(defaultValue = "asc")  String direction) {
        return orderService.searchOrders(
                keyword, page, size, sortBy, direction);
    }
}