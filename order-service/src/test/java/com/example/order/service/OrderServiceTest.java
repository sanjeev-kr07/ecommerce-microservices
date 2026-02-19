package com.example.order.service;

import com.example.order.client.InventoryClient;
import com.example.order.client.dto.InventoryResponse;
import com.example.order.dto.PlaceOrderRequest;
import com.example.order.entity.Order;
import com.example.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void placeOrder_success() {

        PlaceOrderRequest req = new PlaceOrderRequest();
        req.setProductId(1001L);
        req.setQuantity(2);

        InventoryResponse inventory = new InventoryResponse();
        inventory.setProductId(1001L);
        inventory.setProductName("Laptop");

        when(inventoryClient.getInventory(1001L)).thenReturn(inventory);
        doNothing().when(inventoryClient).reserveInventory(1001L, 2);

        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setProductId(1001L);
        savedOrder.setProductName("Laptop");
        savedOrder.setQuantity(2);
        savedOrder.setStatus("PLACED");
        savedOrder.setOrderDate(LocalDate.now());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        var response = orderService.placeOrder(req);

        assertThat(response.getOrderId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo("PLACED");

        verify(inventoryClient).getInventory(1001L);
        verify(inventoryClient).reserveInventory(1001L, 2);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_invalidQuantity_shouldFail() {

        PlaceOrderRequest req = new PlaceOrderRequest();
        req.setProductId(1001L);
        req.setQuantity(0);

        assertThatThrownBy(() -> orderService.placeOrder(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid quantity");

        verifyNoInteractions(inventoryClient);
        verifyNoInteractions(orderRepository);
    }
}