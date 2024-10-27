package com.jakkapat.order_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jakkapat.order_service.client.InventoryClient;
import com.jakkapat.order_service.dto.OrderRequest;
import com.jakkapat.order_service.model.Order;
import com.jakkapat.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {
        if (!inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity())) {
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " is out of stock");
        }
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        orderRepository.save(order);
    }

}
