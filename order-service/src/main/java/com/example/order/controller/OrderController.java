package com.example.order.controller;

import com.example.order.dto.PlaceOrderRequest;
import com.example.order.dto.PlaceOrderResponse;
import com.example.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PlaceOrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        PlaceOrderResponse response = service.placeOrder(request);
        return ResponseEntity.ok(response);
    }
}