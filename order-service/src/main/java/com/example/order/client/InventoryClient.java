package com.example.order.client;

import com.example.order.client.dto.InventoryResponse;
import com.example.order.client.dto.InventoryUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class InventoryClient {

    private final WebClient webClient;

    public InventoryClient(WebClient inventoryWebClient) {
        this.webClient = inventoryWebClient;
    }

    public InventoryResponse getInventory(Long productId) {
        try {
            return webClient.get()
                    .uri("/inventory/v1/{productId}?productType=DEFAULT", productId)
                    .retrieve()
                    .bodyToMono(InventoryResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Inventory service unavailable :"+e.getMessage());
            throw new IllegalStateException("Inventory service unavailable");
        }
    }

    public void reserveInventory(Long productId, int quantity) {
        InventoryUpdateRequest req = new InventoryUpdateRequest();
        req.setProductId(productId);
        req.setQuantity(quantity);
        req.setProductType("DEFAULT");

        webClient.post()
                .uri("/inventory/v1/update")
                .bodyValue(req)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}