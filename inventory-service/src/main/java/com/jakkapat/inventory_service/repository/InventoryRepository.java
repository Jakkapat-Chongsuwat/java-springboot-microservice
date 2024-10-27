package com.jakkapat.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jakkapat.inventory_service.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    public boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skucode, Integer quantity);

}
