package com.ecommerce.notification.consumer;

import com.ecommerce.notification.service.NotificationService;
import com.ecommerce.common.model.InventoryEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class InventoryEventConsumer {
    
    @Autowired
    private NotificationService notificationService;
    
    private KafkaConsumer<String, String> consumer;
    private ObjectMapper objectMapper;
    private ExecutorService executor;
    private volatile boolean running = false;
    
    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("inventory-events"));
        
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        executor = Executors.newSingleThreadExecutor();
        running = true;
        
        executor.submit(this::consumeInventoryEvents);
    }
    
    private void consumeInventoryEvents() {
        while (running) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        InventoryEvent event = objectMapper.readValue(record.value(), InventoryEvent.class);
                        System.out.println("Recebido evento de inventário: " + event.getEventType() + 
                                         " para pedido " + event.getOrderId());
                        
                        // Processar notificação (RF-4)
                        notificationService.processInventoryEvent(event);
                        
                        // Commit manual após processamento bem-sucedido
                        consumer.commitSync();
                        
                    } catch (Exception e) {
                        System.err.println("Erro ao processar evento de inventário: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro no consumer de eventos: " + e.getMessage());
            }
        }
    }
    
    @PreDestroy
    public void cleanup() {
        running = false;
        if (consumer != null) {
            consumer.close();
        }
        if (executor != null) {
            executor.shutdown();
        }
    }
}