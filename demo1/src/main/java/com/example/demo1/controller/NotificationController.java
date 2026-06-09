package com.example.demo1.controller;

import com.example.demo1.entity.Notification;
import com.example.demo1.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // ✅ Get All
    @Operation(
            summary = "Get All Notifications",
            description = "Fetch all notifications"
    )

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notifications fetched successfully"
            )
    })

    @GetMapping
    public List<Notification> getAll() {

        return notificationService.getAll();
    }

    // ✅ Get By ID
    @Operation(
            summary = "Get Notification By ID",
            description = "Fetch notification using ID"
    )

    @GetMapping("/{id}")
    public Notification getById(
            @PathVariable Long id) {

        return notificationService
                .getById(id);
    }

    // ✅ Get By Type
    @Operation(
            summary = "Get Notifications By Type",
            description = "Fetch notifications by type"
    )

    @GetMapping("/type/{type}")
    public List<Notification> getByType(
            @PathVariable String type) {

        return notificationService
                .getByType(type);
    }

    // ✅ Get Unread
    @Operation(
            summary = "Get Unread Notifications",
            description = "Fetch unread notifications"
    )

    @GetMapping("/unread")
    public List<Notification> getUnread() {

        return notificationService
                .getUnread();
    }

    // ✅ Mark As Read
    @Operation(
            summary = "Mark Notification As Read",
            description = "Update read status"
    )

    @PutMapping("/read/{id}")
    public Notification markAsRead(
            @PathVariable Long id) {

        return notificationService
                .markAsRead(id);
    }

    // ✅ Delete
    @Operation(
            summary = "Delete Notification",
            description = "Delete notification by ID"
    )

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id) {

        notificationService.delete(id);

        return "Notification deleted successfully!";
    }
}