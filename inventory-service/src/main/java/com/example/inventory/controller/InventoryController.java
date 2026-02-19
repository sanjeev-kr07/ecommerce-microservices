package com.example.inventory.controller;

import com.example.inventory.dto.InventoryDto;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.InventoryUpdateRequest;
import com.example.inventory.entity.Inventory;
import com.example.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @GetMapping("/v1/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId,
                                                          @RequestParam(defaultValue = "DEFAULT") String productType) {

        InventoryResponse response = service.getInventory(productId, productType);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/v1/update")
    public ResponseEntity<?> updateInventory(@RequestBody InventoryUpdateRequest request) {
        service.reserve(request.getProductId(), request.getQuantity(), request.getProductType());
        return ResponseEntity.ok().body(
                java.util.Map.of("message", "Inventory updated successfully")
        );
    }
}