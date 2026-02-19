package com.example.order.client.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReserveInventoryResponse {
    private List<Long> reservedFromBatchIds;
}
