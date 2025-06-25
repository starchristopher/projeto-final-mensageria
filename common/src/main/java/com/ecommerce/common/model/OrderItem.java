package com.ecommerce.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderItem {
    private final String productId;
    private final int quantity;
    private final double price;

    @JsonCreator
    public OrderItem(
            @JsonProperty("productId") String productId,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("price") double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "OrderItem{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}