package com.ecommerce.order.controller;

import com.ecommerce.order.service.OrderService;
import com.ecommerce.common.model.Order;
import com.ecommerce.common.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(request.getItems(), request.getCustomerEmail());
            return ResponseEntity.ok(Map.of(
                "success", true,
                "orderId", order.getOrderId(),
                "message", "Pedido criado e enviado para processamento"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Erro ao criar pedido: " + e.getMessage()
            ));
        }
    }

    public static class CreateOrderRequest {
        private List<OrderItem> items;
        private String customerEmail;

        // Getters e Setters
        public List<OrderItem> getItems() { return items; }
        public void setItems(List<OrderItem> items) { this.items = items; }
        public String getCustomerEmail() { return customerEmail; }
        public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    }
}