package com.ecommerce.inventory.service;

import com.ecommerce.inventory.producer.InventoryEventProducer;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.common.model.Order;
import com.ecommerce.common.model.OrderItem;
import com.ecommerce.common.model.InventoryEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private InventoryEventProducer inventoryEventProducer;
    
    public void processOrder(Order order) {
        try {
            boolean allItemsAvailable = true;
            
            // Verificar disponibilidade de estoque para todos os itens
            for (OrderItem item : order.getItems()) {
                if (!inventoryRepository.hasStock(item.getProductId(), item.getQuantity())) {
                    allItemsAvailable = false;
                    break;
                }
            }
            
            InventoryEvent event;
            
            if (allItemsAvailable) {
                // Reservar estoque para todos os itens
                for (OrderItem item : order.getItems()) {
                    inventoryRepository.reserveStock(item.getProductId(), item.getQuantity());
                }
                
                event = new InventoryEvent(
                    order.getOrderId(),
                    InventoryEvent.EventType.STOCK_RESERVED,
                    "Estoque reservado com sucesso para o pedido " + order.getOrderId(),
                    LocalDateTime.now()
                );
                
                System.out.println("✅ Estoque reservado para pedido: " + order.getOrderId());
            } else {
                event = new InventoryEvent(
                    order.getOrderId(),
                    InventoryEvent.EventType.STOCK_UNAVAILABLE,
                    "Estoque insuficiente para o pedido " + order.getOrderId(),
                    LocalDateTime.now()
                );
                
                System.out.println("❌ Estoque insuficiente para pedido: " + order.getOrderId());
            }
            
            // Publicar evento de inventário
            inventoryEventProducer.sendInventoryEvent(event);
            
        } catch (Exception e) {
            System.err.println("Erro ao processar pedido " + order.getOrderId() + ": " + e.getMessage());
        }
    }
}