# Ecommerce — versão em microsserviços

E-commerce dividido em microsserviços Spring Boot, com API Gateway, autenticação JWT, mensageria com Kafka,
armazenamento de imagens no MinIO e pagamentos pelo Mercado Pago (cartão de crédito, débito e Pix).

Faz parte de um TCC que compara a arquitetura de microsserviços com a monolítica. A versão monolítica tem as
mesmas regras de negócio, endpoints, contratos JSON e códigos de status, para que os dois modelos possam ser
comparados nos mesmos cenários.

## Arquitetura

```
                         ┌──────────────────────┐
     cliente  ─────────► │  api-gateway  :8080  │
                         └──────────┬───────────┘
        ┌─────────────┬─────────────┼──────────────┬──────────────┐
        ▼             ▼             ▼              ▼              ▼
   auth :8081   participante   produto :8083   loja :8084    pedido :8085
                   :8082
        │             ▲             ▲  ▲           ▲              │
        │  Kafka      │             │  │  Kafka    │  HTTP        │
        └─────────────┘             │  └───────────┼──────────────┤
         usuarios-criados           │ produtos-vendidos           │
                                    └──────── HTTP (Feign) ───────┘
```

| Serviço | Porta | Responsabilidade | Banco |
|---|---|---|---|
| `api-gateway` | 8080 | Ponto de entrada único; roteia por caminho e valida o JWT | — |
| `auth` | 8081 | Cadastro, login e emissão de tokens (usuário e serviço) | `ecommerceauth` |
| `participante` | 8082 | Dados pessoais do cliente (nome, CPF, nascimento) | `ecommerceparticipante` |
| `produto` | 8083 | Catálogo, variações (tamanho/cor), estoque e imagens | `ecommerceproduto` |
| `loja` | 8084 | Personalização (cor, logo) e token do Mercado Pago | `ecommerceloja` |
| `pedido` | 8085 | Pedidos, pagamentos no Mercado Pago e webhook | `ecommercepedido` |

Cada serviço tem seu próprio banco no mesmo Postgres. O módulo `servicos/` não é um serviço: guarda os
`docker-compose` da infraestrutura, o `schema.sql` e o script de seed.

### Comunicação entre serviços

| De → Para | Como | Quando |
|---|---|---|
| `auth` → `participante` | Kafka, tópico `usuarios-criados` (assíncrono) | Ao cadastrar usuário, o participante é criado com os dados pessoais |
| `pedido` → `produto` | HTTP (OpenFeign), repassando o token do usuário | Validar produtos, variações, estoque e obter o preço do catálogo |
| `pedido` → `participante` | HTTP (OpenFeign), repassando o token do usuário | Descobrir o participante do usuário logado |
| `pedido` → `loja` | HTTP (OpenFeign), com token de serviço | Buscar o token do Mercado Pago a cada pagamento |
| `pedido` → `produto` | Kafka, tópico `produtos-vendidos` (assíncrono) | Pagamento aprovado: baixa de estoque |

O token de serviço é obtido pelo `pedido` em `POST /auth/token/servico` (client id/secret) e tem o papel
`ROLE_SERVICE`. Esse endpoint e a leitura do token do Mercado Pago são bloqueados no gateway: só funcionam
de serviço para serviço.

## Tecnologias

- Java 21, Spring Boot 3.5 (auth em 3.2), Spring Security (OAuth2 Resource Server), Spring Data JPA
- Spring Cloud Gateway e OpenFeign
- PostgreSQL 17, Apache Kafka (Confluent 7.7), MinIO
- MapStruct e Lombok
- SDK Java do Mercado Pago 2.8
- JUnit 5, Mockito e Spring Security Test

## Como rodar

### 1. Infraestrutura

```bash
cd servicos/database && docker compose up -d   # Postgres :5556 (cria os 5 bancos pelo schema.sql)
cd servicos/broker   && docker compose up -d   # Kafka :29092, Zookeeper :22181, Kafka UI :8090
cd servicos/bucket   && docker compose up -d   # MinIO :9100 (console :9002), cria os buckets
```

O `schema.sql` só roda na primeira subida, com a pasta `servicos/database/data` vazia.

### 2. Serviços

Em um terminal para cada serviço (precisa de JDK 21):

```bash
cd auth && ./mvnw spring-boot:run
cd participante && ./mvnw spring-boot:run
cd produto && ./mvnw spring-boot:run
cd loja && ./mvnw spring-boot:run
cd pedido && ./mvnw spring-boot:run
cd api-gateway && ./mvnw spring-boot:run
```

Variáveis de ambiente opcionais (os valores padrão servem para desenvolvimento):

| Variável | Usada por | Padrão |
|---|---|---|
| `JWT_SECRET` | todos | `dev-secret-key-12345678901234567890` |
| `PEDIDO_CLIENT_SECRET` | `auth` e `pedido` | `dev-pedido-secret` |

### 3. Dados de teste

```bash
bash servicos/seed/seed.sh
```

Cria um usuário admin e um cliente (as credenciais estão no script), o participante do cliente e três
produtos com imagem: Caneca, Camiseta (com variações P/M/G) e Boné. Pode ser executado mais de uma vez.

### 4. Token do Mercado Pago

O access token fica guardado no serviço `loja` e é cadastrado por um administrador:

```http
PUT /loja/mercadopago-token
Authorization: Bearer <token de admin>
Content-Type: application/json

{ "token": "TEST-..." }
```

Para receber o webhook, configure no painel do Mercado Pago uma URL pública (por exemplo, via ngrok)
apontando para `/webhook/mercadopago` do gateway.

## Endpoints (via gateway, porta 8080)

| Método | Caminho | Acesso | Descrição |
|---|---|---|---|
| POST | `/auth/register` | público | Cadastra usuário e devolve o token |
| POST | `/auth/login` | público | Login, devolve o token |
| GET | `/participante/usuario/{idUsuario}` | o próprio usuário ou ADMIN | Participante de um usuário |
| GET | `/participante/{id}` | ADMIN | Participante por id |
| GET | `/participante` | ADMIN | Lista participantes |
| GET | `/produto/buscaTodos` | público | Lista produtos |
| GET | `/produto/busca/{id}` | público | Busca produto |
| GET | `/produto/buscaIds?ids=1,2` | público | Busca vários produtos |
| GET | `/produto/buscaTodosComEstoque` | público | Produtos com estoque, só com as variações disponíveis |
| POST | `/produto` | ADMIN | Cadastra produto (multipart, com imagem e variações) |
| PUT | `/produto` | ADMIN | Altera produto (multipart) |
| DELETE | `/produto/{id}` | ADMIN | Exclui produto |
| GET | `/loja` | público | Cor e logo da loja |
| PUT | `/loja` | ADMIN | Altera cor e logo (multipart) |
| PUT | `/loja/mercadopago-token` | ADMIN | Cadastra o token do Mercado Pago |
| POST | `/pedido` | USER ou ADMIN | Cria pedido e pagamento |
| POST | `/webhook/mercadopago` | público | Notificações de pagamento do Mercado Pago |

Os erros seguem o formato `{"campo": "...", "mensagem": "...", "dataHora": "..."}`.

### Exemplo de pedido

```json
POST /pedido
{
  "itensPedido": [
    { "idProduto": 2, "idProdutoVariacao": 1, "quantidade": 2 },
    { "idProduto": 1, "quantidade": 1 }
  ],
  "endereco": { "cep": "01001000", "rua": "Praça da Sé", "bairro": "Sé", "numero": "1", "cidade": "São Paulo", "uf": "SP" },
  "pagamento": {
    "tipoPagamento": "PIX",
    "emailPagador": "cliente@email.com",
    "primeiroNomePagador": "Maria",
    "segundoNomePagador": "Silva",
    "cpfCnpjPagador": "52998224725"
  }
}
```

Para cartão, use `"tipoPagamento": "CREDITO"` ou `"DEBITO"` e informe `bandeiraCartao` (`VISA`, `MASTER`,
`ELO`), `tokenCartao` (gerado no front com a Public Key do Mercado Pago) e, no crédito, `parcelas`.
O preço e a descrição dos itens vêm sempre do catálogo; valores enviados pelo cliente são ignorados.
No Pix, a resposta traz o QR Code (`qrCodePix` e `qrCodePixBase64`).

## Fluxo de pagamento

1. O `pedido` valida produtos, variações e estoque no `produto` e usa o preço do catálogo.
2. Busca o participante do usuário logado no `participante`.
3. Grava o pedido e cria o pagamento no Mercado Pago com o token buscado na `loja`.
4. Se o Mercado Pago recusar ou falhar, o pedido não fica gravado (rollback) e a resposta é 502.
5. Quando o pagamento é aprovado (na hora ou depois, pelo webhook), o `pedido` publica `produtos-vendidos`
   e o `produto` dá baixa no estoque. A baixa acontece uma única vez por pedido.

| Status no Mercado Pago | Status no pedido |
|---|---|
| `approved` | `APROVADO` |
| `pending` | `PENDENTE` |
| `authorized`, `in_process`, `in_mediation` | `PROCESSANDO` |
| `rejected` | `REJEITADO` |
| `cancelled` | `CANCELADO` |
| `refunded` | `DEVOLVIDO` |
| `charged_back` | `CONTESTADO` |

## Testes

Cada serviço tem seus testes (unitários e de segurança):

```bash
cd pedido && ./mvnw test
```

## Estrutura

```
api-gateway/    roteamento e validação do JWT
auth/           usuários e tokens
participante/   dados pessoais do cliente
produto/        catálogo, estoque e imagens
loja/           personalização e token do Mercado Pago
pedido/         pedidos, pagamentos e webhook
servicos/
  database/     docker-compose do Postgres e schema.sql
  broker/       docker-compose do Kafka, Zookeeper e Kafka UI
  bucket/       docker-compose do MinIO
  seed/         script de dados de teste
```
