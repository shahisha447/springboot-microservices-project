package com.example.demo1.controller;

import com.example.demo1.entity.Manager;
import com.example.demo1.entity.Product;
import com.example.demo1.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Operation(
            summary = "Get All Managers",
            description = "Fetches all managers from database"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Managers retrieved successfully")
    })
    @GetMapping
    public List<Manager> getAll() {
        return managerService.getAllManagers(); // ✅ correct method name
    }

    @Operation(
            summary = "Manager Pagination",
            description = "Fetches Manager page by page"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagination data retrieved successfully")
    })

    @GetMapping("/pagination")
    public Page<Manager> getManagers(Pageable pageable){

        return managerService.getManagers(pageable);
    }

    @Operation(
            summary = "Get Manager By ID",
            description = "Fetch manager details using manager ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager found successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })

    @GetMapping("/{id}")
    public Manager getById(@PathVariable Long id) {
        return managerService.getManagerById(id); // ✅ correct method name
    }


    @Operation(
            summary = "Update Manager",
            description = "Updates existing manager information"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager updated successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })
    @PutMapping("/{id}")
    public Manager update(
            @PathVariable Long id,
            @RequestBody Manager manager) {
        return managerService.updateManager(id, manager); // ✅ correct method name
    }

    @Operation(
            summary = "Delete Manager",
            description = "Deletes manager using manager ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Manager deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Manager not found")
    })

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        managerService.deleteManager(id); // ✅ correct method name
        return "Manager deleted successfully!";
    }

    @GetMapping("/search")
    public Page<Manager> search(
            @RequestParam(defaultValue = "")    String keyword,
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "5")   int size,
            @RequestParam(defaultValue = "id")  String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return managerService.searchManagers(keyword, page, size, sortBy, direction);
    }


}