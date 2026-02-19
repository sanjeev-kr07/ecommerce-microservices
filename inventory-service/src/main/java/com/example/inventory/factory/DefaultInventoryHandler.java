package com.example.inventory.factory;

import com.example.inventory.entity.Inventory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component("DEFAULT")
public class DefaultInventoryHandler implements InventoryHandler {

    @Override
    public List<Inventory> getSortedBatches(List<Inventory> batches) {
        batches.sort(Comparator.comparing(Inventory::getExpiryDate));
        return batches;
    }

    @Override
    public void updateStock(List<Inventory> batches, int requiredQty) {
        int remaining = requiredQty;

        for (Inventory batch : batches) {
            if (remaining <= 0) break;

            int deduct = Math.min(batch.getQuantity(), remaining);
            batch.setQuantity(batch.getQuantity() - deduct);
            remaining -= deduct;
        }

        if (remaining > 0) {
            throw new IllegalStateException("Insufficient stock");
        }
    }
}