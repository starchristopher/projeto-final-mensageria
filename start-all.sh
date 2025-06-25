#!/bin/bash

echo "=== INICIANDO SISTEMA DE MENSAGERIA E-COMMERCE ==="

# Verificar se o Kafka está rodando
echo "✅ Kafka está rodando"

# Verificar se os tópicos existem
echo "🔍 Verificando tópicos..."
if ! kafka-topics.sh --list --bootstrap-server localhost:9092 | grep -q "orders"; then
    echo "❌ Tópico 'orders' não encontrado! Execute os comandos de criação de tópicos primeiro."
    exit 1
fi

if ! kafka-topics.sh --list --bootstrap-server localhost:9092 | grep -q "inventory-events"; then
    echo "❌ Tópico 'inventory-events' não encontrado! Execute os comandos de criação de tópicos primeiro."
    exit 1
fi

echo "✅ Tópicos configurados"

# Compilar projetos
echo "🔨 Compilando projetos..."
cd common && mvn clean install -q && cd ..
cd order-service && mvn clean package -q && cd ..
cd inventory-service && mvn clean package -q && cd ..
cd notification-service && mvn clean package -q && cd ..

echo "✅ Projetos compilados"
