-------- AUTH --------

CREATE DATABASE ecommerceauth;

CREATE TABLE usuario(
    id SERIAL NOT NULL PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(15) NOT NULL DEFAULT 'ROLE_USER' CHECK (role IN ('ROLE_USER','ROLE_ADMIN'))
);
----------------------

-------- PARTICIPANTE --------

CREATE DATABASE ecommerceparticipante;

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

CREATE TABLE loja(
    id VARCHAR(15) NOT NULL PRIMARY KEY,
    cor_primaria VARCHAR(20) NOT NULL,
    mercado_pago_token VARCHAR(255)
);

-- A loja possui apenas uma linha (configuração única)
INSERT INTO loja (id, cor_primaria) VALUES ('default-config', '#FFFFFF');

----------------------