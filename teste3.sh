# Teste 3: Múltiplos pedidos para testar concorrência
echo "🧪 Teste 3: Múltiplos pedidos simultâneos"
echo "════════════════════════════════════"

for i in {1..3}; do
  curl -X POST http://localhost:8081/orders \
    -H "Content-Type: application/json" \
    -d "{
      \"customerEmail\": \"cliente$i@exemplo.com\",
      \"items\": [
        {
          \"productId\": \"PRODUTO_001\",
          \"quantity\": 1,
          \"price\": 29.99
        }
      ]
    }" &
done

wait