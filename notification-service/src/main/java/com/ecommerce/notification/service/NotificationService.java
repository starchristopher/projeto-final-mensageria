package com.ecommerce.notification.service;

import com.ecommerce.common.model.InventoryEvent;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {
    
    // Para garantir idempotência - registrar eventos já processados
    private final Set<String> processedEvents = ConcurrentHashMap.newKeySet();
    
    public void processInventoryEvent(InventoryEvent event) {
        String eventKey = event.getOrderId().toString() + "_" + event.getEventType();
        
        // Verificar idempotência - se já foi processado, ignorar
        if (processedEvents.contains(eventKey)) {
            System.out.println("⚠️ Evento já processado (idempotência): " + eventKey);
            return;
        }
        
        try {
            // Simular envio de notificação
            sendNotification(event);
            
            // Registrar como processado
            processedEvents.add(eventKey);
            
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
            throw e; // Re-throw para permitir reprocessamento
        }
    }
    
    private void sendNotification(InventoryEvent event) {
        String timestamp = event.getProcessedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        
        switch (event.getEventType()) {
            case STOCK_RESERVED:
                sendSuccessNotification(event.getOrderId().toString(), timestamp);
                break;
            case STOCK_UNAVAILABLE:
                sendFailureNotification(event.getOrderId().toString(), timestamp);
                break;
            default:
                System.out.println("❓ Tipo de evento desconhecido: " + event.getEventType());
        }
    }
    
    private void sendSuccessNotification(String orderId, String timestamp) {
        // Simulação de envio de email/SMS de sucesso
        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("   Para: cliente@exemplo.com");
        System.out.println("   Assunto: Pedido Confirmado - " + orderId);
        System.out.println("   Mensagem: Seu pedido foi confirmado e está sendo preparado!");
        System.out.println("   Enviado em: " + timestamp);
        System.out.println("   ═══════════════════════════════════════════");
        
        System.out.println("📱 SMS ENVIADO:");
        System.out.println("   Para: +55 (11) 99999-9999");
        System.out.println("   Mensagem: Pedido " + orderId + " confirmado! Acompanhe pelo app.");
        System.out.println("   Enviado em: " + timestamp);
        System.out.println("   ═══════════════════════════════════════════");
    }
    
    private void sendFailureNotification(String orderId, String timestamp) {
        // Simulação de envio de email/SMS de falha
        System.out.println("📧 EMAIL ENVIADO:");
        System.out.println("   Para: cliente@exemplo.com");
        System.out.println("   Assunto: Pedido Cancelado - " + orderId);
        System.out.println("   Mensagem: Infelizmente não temos estoque suficiente para seu pedido.");
        System.out.println("   Enviado em: " + timestamp);
        System.out.println("   ═══════════════════════════════════════════");
        
        System.out.println("📱 SMS ENVIADO:");
        System.out.println("   Para: +55 (11) 99999-9999");
        System.out.println("   Mensagem: Pedido " + orderId + " cancelado por falta de estoque. Tente novamente.");
        System.out.println("   Enviado em: " + timestamp);
        System.out.println("   ═══════════════════════════════════════════");
    }
}