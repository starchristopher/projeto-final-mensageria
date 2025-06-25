package com.ecommerce.inventory.repository;

import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Repository
public class InventoryRepository {
    
    // Simulação de estoque em memória
    private final Map<String, Integer> stockMap = new ConcurrentHashMap<>();
    
    public InventoryRepository() {
        // Inicializar com alguns produtos de exemplo
        stockMap.put("PRODUTO_001", 100);
        stockMap.put("PRODUTO_002", 50);
        stockMap.put("PRODUTO_003", 25);
        stockMap.put("PRODUTO_004", 0); // Produto sem estoque para testar
    }
    
    public synchronized boolean hasStock(String productId, int quantity) {
        Integer currentStock = stockMap.getOrDefault(productId, 0);
        return currentStock >= quantity;
    }
    
    public synchronized void reserveStock(String productId, int quantity) {
        Integer currentStock = stockMap.getOrDefault(productId, 0);
        if (currentStock >= quantity) {
            stockMap.put(productId, currentStock - quantity);
            System.out.println("Reservado " + quantity + " unidades do produto " + productId + 
                             ". Estoque restante: " + stockMap.get(productId));
        } else {
            throw new IllegalStateException("Estoque insuficiente para produto " + productId);
        }
    }
    
    public Map<String, Integer> getAllStock() {
        return new ConcurrentHashMap<>(stockMap);
    }
}