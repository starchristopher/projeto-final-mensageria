Projeto de mensageria com Java

Integrantes:

- Christopher Star Correa de Oliveira

Tecnologias Utilizadas

Java 17: Linguagem de programação
Spring Boot: Framework para criação dos serviços
Apache Kafka: Message broker para comunicação assíncrona
Maven: Gerenciamento de dependências e build
Jackson: Serialização/deserialização JSON

Como executar:

1 - Tenha o kafka e o zookeper rodando localmente, o kafka deve estar rodando na porta 9092
2 - Execute o script start-all.sh para criação dos tópicos do kafka e build dos serviços com maven
3 - Vá ao diretório /inventory-service e execute mvn spring-boot:run e mantenha aberto
4 - Em outro terminal, vá ao diretório /order-service e execute mvn spring-boot:run e mantenha aberto
5 - Em outro terminal, vá ao diretório /notification-service e exectue mvn spring-boot:run e mantenha aberto
6 - Quando todos os serviços estiverem executando, execute os casos de teste teste1.sh; teste2.sh e teste3.sh

Respostas aos Requisitos Não-Funcionais:

1. Escalabilidade
   Como conseguir escalabilidade com Kafka:

Partições: Os tópicos são criados com 3 partições, permitindo processamento paralelo
Consumer Groups: Cada serviço usa um consumer group diferente, permitindo múltiplas instâncias
Horizontal Scaling: Podemos executar múltiplas instâncias de cada serviço, cada uma processando partições diferentes
Load Balancing: Kafka distribui automaticamente as partições entre consumers do mesmo group

Neste caso, podemos ter várias instâncias separadas do inventory-service, podendo assim escalonar o serviço.

2. Tolerância à Falha
   Conceito: Capacidade do sistema continuar funcionando mesmo quando alguns componentes falham.
   Situação de falha: Se o Inventory-Service cair durante processamento de um pedido.
   Como o Kafka trata:

Commit Manual: Só commitamos offset após processamento completo
At-least-once: Mensagens podem ser reprocessadas se houver falha
Rebalancing: Outros consumers assumem partições do consumer que falhou
Persistência: Mensagens ficam armazenadas no Kafka até serem processadas

3. Idempotência
   Conceito: Capacidade de processar a mesma mensagem múltiplas vezes sem efeitos colaterais.
   Implementação no projeto:

Notification Service: Usa Set para rastrear eventos já processados
Producer: Configurado com enable.idempotence=true
Chaves únicas: Usamos orderId como chave para evitar duplicatas
Transações: Inventory operations são síncronas para consistência
# projeto-final-mensageria
