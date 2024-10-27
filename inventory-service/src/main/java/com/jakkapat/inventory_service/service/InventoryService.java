package com.jakkapat.inventory_service.service;

import org.springframework.stereotype.Service;

import com.jakkapat.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skucode, Integer quantity) {
        return inventoryRepository.existsBySkuCodeAndQuantityIsGreaterThanEqual(skucode, quantity);
    }
}
