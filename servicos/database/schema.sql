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
