-------- AUTH --------

CREATE DATABASE ecommerceauth;
\c ecommerceauth

CREATE TABLE usuario(
    id SERIAL NOT NULL PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(15) NOT NULL DEFAULT 'ROLE_USER' CHECK (role IN ('ROLE_USER','ROLE_ADMIN'))
);
----------------------

-------- PARTICIPANTE --------

CREATE DATABASE ecommerceparticipante;
\c ecommerceparticipante

CREATE TABLE participante(
    id SERIAL NOT NULL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(15) NOT NULL,
    data_nascimento DATE NOT NULL,
    id_usuario BIGINT NOT NULL
);

------------------------------


-------- PRODUTO --------

CREATE DATABASE ecommerceproduto;
\c ecommerceproduto

CREATE TABLE produto (
    id SERIAL NOT NULL PRIMARY KEY,
    descricao VARCHAR(255),
    valor_unitario NUMERIC(10,2),
    quantidade INTEGER
);

CREATE TABLE produto_variacao (
   id SERIAL NOT NULL PRIMARY KEY,
   tamanho VARCHAR(50),
   cor VARCHAR(50),
   quantidade INTEGER,
   produto_id BIGINT,

   CONSTRAINT fk_produto
       FOREIGN KEY (produto_id)
            REFERENCES produto (id)
            ON DELETE CASCADE
);

--------------------------


-------- LOJA --------

CREATE DATABASE ecommerceloja;
\c ecommerceloja

CREATE TABLE loja(
    id VARCHAR(15) NOT NULL PRIMARY KEY,
    cor_primaria VARCHAR(20) NOT NULL,
    mercado_pago_token VARCHAR(255)
);

-- A loja possui apenas uma linha (configuração única)
INSERT INTO loja (id, cor_primaria) VALUES ('default-config', '#FFFFFF');

----------------------


-------- PEDIDO --------

CREATE DATABASE ecommercepedido;
\c ecommercepedido

CREATE TABLE pedido(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    id_participante BIGINT NOT NULL,
    valor_total NUMERIC(10,2)
);

CREATE TABLE item_pedido(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    descricao VARCHAR(255),
    valor_unitario NUMERIC(10,2),
    quantidade INTEGER,
    valor_total_item NUMERIC(10,2),
    id_produto BIGINT,
    id_produto_variacao BIGINT,
    pedido_id BIGINT,

    CONSTRAINT fk_item_pedido
        FOREIGN KEY (pedido_id)
            REFERENCES pedido (id)
            ON DELETE CASCADE
);

CREATE TABLE endereco_pedido(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    cep VARCHAR(10),
    rua VARCHAR(255),
    bairro VARCHAR(100),
    numero VARCHAR(20),
    cidade VARCHAR(100),
    uf VARCHAR(2),
    pedido_id BIGINT UNIQUE,

    CONSTRAINT fk_endereco_pedido
        FOREIGN KEY (pedido_id)
            REFERENCES pedido (id)
            ON DELETE CASCADE
);

CREATE TABLE pagamento_pedido(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    status_pagamento VARCHAR(20) CHECK (status_pagamento IN ('PENDENTE','PROCESSANDO','APROVADO','REJEITADO','CANCELADO','DEVOLVIDO','CONTESTADO')),
    observacao_status VARCHAR(255),
    bandeira_cartao VARCHAR(10) CHECK (bandeira_cartao IN ('VISA','MASTER','ELO')),
    tipo_pagamento VARCHAR(10) CHECK (tipo_pagamento IN ('CREDITO','DEBITO','PIX')),
    valor_pagamento NUMERIC(10,2),
    parcelas INTEGER,
    mercado_pago_id BIGINT,
    pedido_id BIGINT UNIQUE,

    CONSTRAINT fk_pagamento_pedido
        FOREIGN KEY (pedido_id)
            REFERENCES pedido (id)
            ON DELETE CASCADE
);

------------------------
