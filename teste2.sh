# Teste 2: Pedido com estoque insuficiente
echo "🧪 Teste 2: Pedido com estoque insuficiente"
echo "═══════════════════════════════════════"

curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerEmail": "cliente2@exemplo.com",
    "items": [
      {
        "productId": "PRODUTO_003",
        "quantity": 50,
        "price": 19.99
      },
      {
        "productId": "PRODUTO_004",
        "quantity": 1,
        "price": 99.99
      }
    ]
  }'

echo ""
echo ""

# Aguardar processamento
sleep 3