package com.example.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class InventoryResponse {
    private Long productId;
    private String productName;
    private List<InventoryDto> batches;
}