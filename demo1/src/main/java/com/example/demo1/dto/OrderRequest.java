package com.example.demo1.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long managerId;  // ✅ only these 2
    private Long productId;
}