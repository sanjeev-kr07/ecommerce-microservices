package com.example.order.client.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InventoryBatchDto {
    private Long batchId;
    private Integer quantity;
    private LocalDate expiryDate;
}
