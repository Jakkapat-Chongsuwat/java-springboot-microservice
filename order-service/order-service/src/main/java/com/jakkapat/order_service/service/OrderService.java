package com.jakkapat.order_service.service;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.jakkapat.order_service.client.InventoryClient;
import com.jakkapat.order_service.dto.OrderRequest;
import com.jakkapat.order_service.event.OrderPlacedEvent;
import com.jakkapat.order_service.model.Order;
import com.jakkapat.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

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

        // Send Message to Kafka
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setOrderNumber(order.getOrderNumber());
        orderPlacedEvent.setEmail(orderRequest.userDetails().email());
        orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
        orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());

        log.info("Sending OrderPlacedEvent to Kafka for topic order-placed: {}", orderPlacedEvent);
        kafkaTemplate.send("order-placed", orderPlacedEvent);
        log.info("OrderPlacedEvent sent successfully to Kafka for topic order-placed: {}", orderPlacedEvent);
    }
}
