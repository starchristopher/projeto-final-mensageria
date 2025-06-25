package com.ecommerce.common.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class InventoryEvent {
    private final UUID orderId;
    private final EventType eventType;
    private final String message;
    private final LocalDateTime processedAt;

    @JsonCreator
    public InventoryEvent(
            @JsonProperty("orderId") UUID orderId,
            @JsonProperty("eventType") EventType eventType,
            @JsonProperty("message") String message,
            @JsonProperty("processedAt") LocalDateTime processedAt) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.message = message;
        this.processedAt = processedAt;
    }

    public enum EventType {
        STOCK_RESERVED, STOCK_UNAVAILABLE
    }

    // Getters
    public UUID getOrderId() { return orderId; }
    public EventType getEventType() { return eventType; }
    public String getMessage() { return message; }
    public LocalDateTime getProcessedAt() { return processedAt; }

    @Override
    public String toString() {
        return "InventoryEvent{" +
                "orderId=" + orderId +
                ", eventType=" + eventType +
                ", message='" + message + '\'' +
                ", processedAt=" + processedAt +
                '}';
    }
}