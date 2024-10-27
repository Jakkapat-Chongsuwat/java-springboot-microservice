package com.jakkapat.inventory_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jakkapat.inventory_service.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/is-in-stock")
    public boolean isInStock(String skuCode, Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

}
