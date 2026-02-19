package com.example.inventory.factory;

import com.example.inventory.entity.Inventory;

import java.util.List;

public interface InventoryHandler {

    List<Inventory> getSortedBatches(List<Inventory> batches);

    void updateStock(List<Inventory> batches, int requiredQty);
}