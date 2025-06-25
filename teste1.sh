# Teste 1: Pedido com estoque disponível
echo ""
echo "🧪 Teste 1: Pedido com estoque disponível"
echo "═══════════════════════════════════════"

curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerEmail": "cliente1@exemplo.com",
    "items": [
      {
        "productId": "PRODUTO_001",
        "quantity": 2,
        "price": 29.99
      },
      {
        "productId": "PRODUTO_002",
        "quantity": 1,
        "price": 49.99
      }
    ]
  }'

echo ""
echo ""

# Aguardar processamento
sleep 3