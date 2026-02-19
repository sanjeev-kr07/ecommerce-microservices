package com.example.inventory.controller;

import com.example.inventory.dto.InventoryDto;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.dto.InventoryUpdateRequest;
import com.example.inventory.dto.ReserveInventoryResponse;
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
    public ResponseEntity<ReserveInventoryResponse> reserve(@RequestBody InventoryUpdateRequest req) {

        List<Long> batchIds = service.reserveAndReturnBatchIds(
                req.getProductId(),
                req.getQuantity()
        );

        ReserveInventoryResponse resp = new ReserveInventoryResponse();
        resp.setReservedFromBatchIds(batchIds);

        return ResponseEntity.ok(resp);
    }
}