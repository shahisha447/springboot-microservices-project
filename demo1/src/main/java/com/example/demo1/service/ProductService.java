package com.example.demo1.service;

import com.example.demo1.entity.Product;
import com.example.demo1.repository.ProductRepository;
import com.example.demo1.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private AuditLogService auditLogService; // ✅

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long id, Product updated) {
        Product product = getProductById(id);
        String oldName = product.getName();
        product.setName(updated.getName());
        product.setPhone(updated.getPhone());
        product.setAddress(updated.getAddress());
        Product saved = productRepository.save(product);

        // ✅ Log UPDATE
        auditLogService.log(
                "PRODUCT",
                saved.getUsername(),
                "UPDATE",
                "product",
                "Product updated name from: " + oldName
                        + " to: " + saved.getName()
        );

        return saved;
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);

        // ✅ Log DELETE
        auditLogService.log(
                "PRODUCT",
                product.getUsername(),
                "DELETE",
                "product",
                "Product deleted: " + product.getName()
        );

        productRepository.deleteById(id);
    }

    public Page<Product> searchProducts(String keyword, int page,
                                        int size, String sortBy,
                                        String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository
                .findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCaseOrAddressContainingIgnoreCase(
                        keyword, keyword, keyword, pageable);
    }
    // ✅ Add this method to ProductService.java
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}