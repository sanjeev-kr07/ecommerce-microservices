package com.example.inventory.dto;

import lombok.Data;

@Data
public class InventoryUpdateRequest {
    private Long productId;
    private int quantity;
    private String productType;
}