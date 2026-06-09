package com.example.demo1.controller;

import com.example.demo1.entity.UserInfo;
import com.example.demo1.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {

    @Autowired private UserInfoRepository userInfoRepository;

    @Operation(
            summary = "Get All User Information",
            description = "Fetches all user records from database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User records retrieved successfully")
    })
    @GetMapping
    public List<UserInfo> getAll() {
        return userInfoRepository.findAll();
    }

    @Operation(
            summary = "Get User Information By ID",
            description = "Fetch user details using user ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information found successfully"),
            @ApiResponse(responseCode = "404", description = "User information not found")
    })
    @GetMapping("/{id}")
    public UserInfo getById(@PathVariable Long id) {
        return userInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserInfo not found"));
    }

    @Operation(
            summary = "Delete User Information",
            description = "Deletes user record using user ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User information deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User information not found")
    })
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        userInfoRepository.deleteById(id);
        return "UserInfo deleted!";
    }
}