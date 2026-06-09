package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Create User
    @Operation(
            summary = "Create User",
            description = "Creates a new user in the database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @PostMapping
    public User create(
            @Valid @RequestBody User user) {

        return userService.createUser(user);
    }

    // ✅ Get All Users
    @Operation(
            summary = "Get All Users",
            description = "Fetches all users from database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    // ✅ Get User By ID
    @Operation(
            summary = "Get User By ID",
            description = "Fetches user details using user ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public User getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }

    // ✅ Update User
    @Operation(
            summary = "Update User",
            description = "Updates existing user information"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user) {

        return userService.updateUser(id, user);
    }

    // ✅ Delete User
    @Operation(
            summary = "Delete User",
            description = "Deletes user using user ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public String deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return "User deleted successfully!";
    }

    // ✅ Search + Pagination
    @Operation(
            summary = "Search Users",
            description = "Searches users using keyword with pagination support"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public Page<User> searchUsers(

            @RequestParam(defaultValue = "")
            String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {

        return userService.searchUsers(
                keyword,
                page,
                size
        );
    }
    @Operation(
            summary = "User Pagination",
            description = "Fetches users page by page using pagination"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagination data retrieved successfully")
    })
        @GetMapping("/pagination")
        public Page<User> getUsers(Pageable pageable){

            return userService.getUsers(pageable);

    }
}