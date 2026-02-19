package com.example.inventory.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InventoryDto {
    private Long batchId;
    private Integer quantity;
    private LocalDate expiryDate;

}
