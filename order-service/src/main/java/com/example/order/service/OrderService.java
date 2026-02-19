package com.example.order.service;

import com.example.order.client.InventoryClient;
import com.example.order.client.dto.InventoryResponse;
import com.example.order.client.dto.ReserveInventoryResponse;
import com.example.order.dto.PlaceOrderRequest;
import com.example.order.dto.PlaceOrderResponse;
import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
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
        ReserveInventoryResponse reserveResp = new ReserveInventoryResponse();
        try {
            reserveResp = inventoryClient.reserveInventory(
                    request.getProductId(),
                    request.getQuantity()
            );
        } catch (Exception e) {
            log.error("Unable to reserve inventory "+e.getMessage());
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

        PlaceOrderResponse resp = new PlaceOrderResponse();
        resp.setOrderId(saved.getOrderId());
        resp.setProductId(saved.getProductId());
        resp.setProductName(saved.getProductName());
        resp.setQuantity(saved.getQuantity());
        resp.setStatus(saved.getStatus());
        resp.setReservedFromBatchIds(reserveResp.getReservedFromBatchIds());
        resp.setMessage("Order placed. Inventory reserved.");

        return resp;
    }
}