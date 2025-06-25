package com.ecommerce.order.service;

import com.ecommerce.order.producer.OrderProducer;
import com.ecommerce.common.model.Order;
import com.ecommerce.common.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderProducer orderProducer;

    public Order createOrder(List<OrderItem> items, String customerEmail) {
        // Gerar UUID e timestamp (RF-2)
        UUID orderId = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();
        
        Order order = new Order(orderId, timestamp, items, customerEmail);
        
        // Publicar no tópico orders
        orderProducer.sendOrder(order);
        
        System.out.println("Pedido criado: " + order);
        
        return order;
    }
}