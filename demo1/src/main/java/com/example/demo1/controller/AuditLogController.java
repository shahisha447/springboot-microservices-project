package com.example.demo1.controller;

import com.example.demo1.entity.AuditLog;
import com.example.demo1.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/api/auditlog")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    // ✅ Get All
    @Operation(
            summary = "Get All Audit Logs",
            description = "Fetches all audit log records from database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully")
    })
    @GetMapping
    public List<AuditLog> getAll() {
        return auditLogService.getAll();
    }

    // ✅ Get by username
    @Operation(
            summary = "Get Audit Logs By Username",
            description = "Fetches audit logs using username"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Username not found")
    })
    @GetMapping("/username/{username}")
    public List<AuditLog> getByUsername(
            @PathVariable String username) {
        return auditLogService.getByUsername(username);
    }

    // ✅ Get by action
    @Operation(
            summary = "Get Audit Logs By Action",
            description = "Fetches audit logs using action type"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Action not found")
    })
    @GetMapping("/action/{action}")
    public List<AuditLog> getByAction(
            @PathVariable String action) {
        return auditLogService.getByAction(action);
    }

    // ✅ Get by userType
    @Operation(
            summary = "Get Audit Logs By User Type",
            description = "Fetches audit logs using user type"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User type not found")
    })
    @GetMapping("/type/{userType}")
    public List<AuditLog> getByUserType(
            @PathVariable String userType) {
        return auditLogService.getByUserType(userType);
    }

    // ✅ Get by tableName
    @Operation(
            summary = "Get Audit Logs By Table Name",
            description = "Fetches audit logs related to a specific database table"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Table name not found")
    })
    @GetMapping("/table/{tableName}")
    public List<AuditLog> getByTableName(
            @PathVariable String tableName) {
        return auditLogService.getByTableName(tableName);
    }

    // ✅ Search + pagination
    @Operation(
            summary = "Search Audit Logs",
            description = "Searches audit logs using keyword with pagination and sorting"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public Page<AuditLog> search(
            @RequestParam(defaultValue = "")    String keyword,
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "5")   int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return auditLogService.search(
                keyword, page, size, sortBy, direction);
    }
}