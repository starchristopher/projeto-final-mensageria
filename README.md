# Projeto de Mensageria com Java

## Integrantes

- Christopher Star Correa de Oliveira

## Tecnologias Utilizadas

- **Java 17**: Linguagem principal do projeto
- **Spring Boot**: Framework para construção dos microserviços
- **Apache Kafka**: Message broker para comunicação assíncrona
- **Maven**: Gerenciamento de dependências e build
- **Jackson**: Serialização e desserialização de objetos JSON

---

## Como Executar o Projeto

### 1. Pré-requisitos

- Kafka e Zookeeper devem estar rodando localmente.
- Kafka deve estar disponível na porta `9092`.

### 2. Inicialização do Ambiente

Execute o script abaixo para:

- Criar os tópicos Kafka necessários.
- Realizar o build dos serviços com Maven.

```bash
./start-all.sh
```

### 3. Execução dos Serviços

Em três terminais separados, execute os seguintes comandos:

```bash
cd inventory-service
mvn spring-boot:run
```

```bash
cd order-service
mvn spring-boot:run
```

```bash
cd notification-service
mvn spring-boot:run
```

### 4. Execução dos Testes

Com todos os serviços em execução, rode os scripts de teste:

```bash
./teste1.sh
./teste2.sh
./teste3.sh
```

---

## Requisitos Não-Funcionais

### 1. Escalabilidade

O projeto utiliza recursos do Kafka para garantir escalabilidade:

- **Partições**: Cada tópico Kafka possui 3 partições, permitindo processamento paralelo.
- **Consumer Groups**: Cada serviço pertence a um consumer group diferente.
- **Escalonamento Horizontal**: É possível executar várias instâncias de cada serviço.
- **Balanceamento de Carga**: Kafka distribui automaticamente as partições entre os consumidores do mesmo grupo.

### 2. Tolerância à Falha

**Exemplo**: Se o `inventory-service` falhar durante o processamento de uma mensagem.

Mecanismos utilizados:

- **Commit Manual**: Offsets são confirmados apenas após o processamento bem-sucedido.
- **Garantia "at-least-once"**: Mensagens podem ser reprocessadas em caso de falha.
- **Rebalanceamento**: Outro consumer pode assumir a partição de um que falhou.
- **Persistência**: Mensagens permanecem armazenadas até serem processadas com sucesso.

### 3. Idempotência

Medidas adotadas para garantir que a mesma mensagem não cause efeitos duplicados:

- O `notification-service` utiliza um `Set` para rastrear mensagens processadas.
- O produtor Kafka está configurado com `enable.idempotence=true`.
- As mensagens possuem chave (`orderId`) para garantir unicidade.
- As operações no `inventory-service` são síncronas, garantindo consistência de estado.
