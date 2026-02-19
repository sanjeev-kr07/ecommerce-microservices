package com.example.order.dto;

import lombok.Data;

@Data
public class PlaceOrderResponse {
    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String status;
    private String message;

}