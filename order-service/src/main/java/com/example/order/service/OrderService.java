package com.example.order.service;

import com.example.order.client.InventoryClient;
import com.example.order.client.dto.InventoryResponse;
import com.example.order.dto.PlaceOrderRequest;
import com.example.order.dto.PlaceOrderResponse;
import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class OrderService {

    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;

    public OrderService(InventoryClient inventoryClient,
                        OrderRepository orderRepository) {
        this.inventoryClient = inventoryClient;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest request) {

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        // 1. Check inventory
        InventoryResponse inventory;
        try {
            inventory = inventoryClient.getInventory(request.getProductId());
        } catch (Exception e) {
            throw new IllegalStateException("Inventory service unavailable");
        }

        // 2. Reserve inventory
        try {
            inventoryClient.reserveInventory(request.getProductId(), request.getQuantity());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to reserve inventory");
        }

        // 3. Save order
        Order order = new Order();
        order.setProductId(request.getProductId());
        order.setProductName(inventory.getProductName());
        order.setQuantity(request.getQuantity());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        Order saved = orderRepository.save(order);

        // 4. Build response
        PlaceOrderResponse response = new PlaceOrderResponse();
        response.setOrderId(saved.getOrderId());
        response.setProductId(saved.getProductId());
        response.setProductName(saved.getProductName());
        response.setQuantity(saved.getQuantity());
        response.setStatus(saved.getStatus());
        response.setMessage("Order placed. Inventory reserved.");

        return response;
    }
}