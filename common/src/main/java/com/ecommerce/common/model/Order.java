package com.ecommerce.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final LocalDateTime timestamp;
    private final List<OrderItem> items;
    private final String customerEmail;

    @JsonCreator
    public Order(
            @JsonProperty("orderId") UUID orderId,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("items") List<OrderItem> items,
            @JsonProperty("customerEmail") String customerEmail) {
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.items = items;
        this.customerEmail = customerEmail;
    }

    // Getters
    public UUID getOrderId() { return orderId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<OrderItem> getItems() { return items; }
    public String getCustomerEmail() { return customerEmail; }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", timestamp=" + timestamp +
                ", items=" + items +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}