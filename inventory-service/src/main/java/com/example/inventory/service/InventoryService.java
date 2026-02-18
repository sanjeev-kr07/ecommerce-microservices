package com.example.inventory.service;

import com.example.inventory.entity.Inventory;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public List<Inventory> getBatchesByProduct(Long productId) {
        return repository.findByProductIdOrderByExpiryDateAsc(productId);
    }
}