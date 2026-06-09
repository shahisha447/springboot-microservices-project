package com.example.demo1.controller;

import com.example.demo1.entity.Product;
import com.example.demo1.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts(); // ✅ correct method name
    }

    @Operation(
            summary = "Product Pagination",
            description = "Fetches products page by page"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagination data retrieved successfully")
    })
    @GetMapping("/pagination")
    public Page<Product> getProducts(Pageable pageable){

        return productService.getProducts(pageable);
    }

    @Operation(
            summary = "Get Product By ID",
            description = "Fetch product details using product ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })


    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getProductById(id); // ✅ correct method name
    }


    @Operation(
            summary = "Update Product",
            description = "Updates existing product information"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody Product product) {
        return productService.updateProduct(id, product); // ✅ correct method name
    }

    @Operation(
            summary = "Delete Product",
            description = "Deletes product using product ID"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteProduct(id); // ✅ correct method name
        return "Product deleted successfully!";
    }

    @Operation(
            summary = "Search Products",
            description = "Search products using keyword"
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })


    @GetMapping("/search")
    public Page<Product> search(
            @RequestParam(defaultValue = "")     String keyword,
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "5")    int size,
            @RequestParam(defaultValue = "id")   String sortBy,
            @RequestParam(defaultValue = "asc")  String direction) {

        return productService.searchProducts(keyword, page, size, sortBy, direction);
    }

}