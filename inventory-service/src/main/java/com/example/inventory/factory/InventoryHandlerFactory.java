package com.example.inventory.factory;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InventoryHandlerFactory {

    private final Map<String, InventoryHandler> handlers;

    public InventoryHandlerFactory(Map<String, InventoryHandler> handlers) {
        this.handlers = handlers;
    }

    public InventoryHandler getHandler(String type) {
        return handlers.getOrDefault(type, handlers.get("DEFAULT"));
    }
}