package com.example.order.controller;

import com.example.order.client.InventoryClient;
import com.example.order.client.dto.InventoryResponse;
import com.example.order.client.dto.ReserveInventoryResponse;
import com.example.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryClient inventoryClient;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void placeOrder_api_shouldReturn200() throws Exception {

        InventoryResponse inventory = new InventoryResponse();
        inventory.setProductId(1001L);
        inventory.setProductName("Laptop");

        when(inventoryClient.getInventory(1001L)).thenReturn(inventory);

        ReserveInventoryResponse reserveResp = new ReserveInventoryResponse();
        reserveResp.setReservedFromBatchIds(List.of(3L));

        when(inventoryClient.reserveInventory(1001L, 2)).thenReturn(reserveResp);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "productId": 1001,
                      "quantity": 2
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PLACED"))
                .andExpect(jsonPath("$.reservedFromBatchIds[0]").value(3));
    }
}