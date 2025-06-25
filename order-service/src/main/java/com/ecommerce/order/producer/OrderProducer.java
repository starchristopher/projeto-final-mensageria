package com.ecommerce.order.producer;

import com.ecommerce.common.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

@Component
public class OrderProducer {
    
    private KafkaProducer<String, String> producer;
    private ObjectMapper objectMapper;
    
    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        producer = new KafkaProducer<>(props);
        
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }
    
    public void sendOrder(Order order) {
        try {
            String orderJson = objectMapper.writeValueAsString(order);
            ProducerRecord<String, String> record = new ProducerRecord<>(
                "orders", 
                order.getOrderId().toString(), 
                orderJson
            );
            
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    System.err.println("Erro ao enviar pedido: " + exception.getMessage());
                } else {
                    System.out.println("Pedido enviado com sucesso para tópico: " + metadata.topic() + 
                                     ", partição: " + metadata.partition() + 
                                     ", offset: " + metadata.offset());
                }
            });
        } catch (Exception e) {
            System.err.println("Erro ao serializar pedido: " + e.getMessage());
        }
    }
    
    @PreDestroy
    public void cleanup() {
        if (producer != null) {
            producer.close();
        }
    }
}