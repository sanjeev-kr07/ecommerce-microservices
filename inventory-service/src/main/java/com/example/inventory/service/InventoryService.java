package com.example.inventory.service;

import com.example.inventory.dto.InventoryDto;
import com.example.inventory.dto.InventoryResponse;
import com.example.inventory.entity.Inventory;
import com.example.inventory.factory.InventoryHandler;
import com.example.inventory.factory.InventoryHandlerFactory;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final InventoryRepository repository;
    private final InventoryHandlerFactory factory;

    public InventoryService(InventoryRepository repository,
                            InventoryHandlerFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public InventoryResponse getInventory(Long productId, String productType) {

        if (productId == null) {
            throw new IllegalArgumentException("ProductId cannot be null");
        }

        List<Inventory> batches =
                repository.findByProductIdOrderByExpiryDateAsc(productId);

        if (batches.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        InventoryHandler handler = factory.getHandler(productType);

        List<Inventory> sortedBatches = handler.getSortedBatches(batches);

        InventoryResponse response = new InventoryResponse();

        response.setProductId(productId);
        response.setProductName(batches.get(0).getProductName());
        response.setBatches(
                sortedBatches.stream().map(this::toDto).collect(Collectors.toList())
        );

        return response;
    }

    @Transactional
    public void reserve(Long productId, int qty, String productType) {

        if (qty <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }

        List<Inventory> batches =
                repository.findByProductIdOrderByExpiryDateAsc(productId);

        if (batches.isEmpty()) {
            throw new IllegalArgumentException("Product not found");
        }

        InventoryHandler handler = factory.getHandler(productType);
        handler.updateStock(batches, qty);

        repository.saveAll(batches);
    }

    private InventoryDto toDto(Inventory batch) {
        InventoryDto dto = new InventoryDto();
        dto.setBatchId(batch.getBatchId());
        dto.setQuantity(batch.getQuantity());
        dto.setExpiryDate(batch.getExpiryDate());
        return dto;
    }
}