CREATE DATABASE ecommerceauth;

-------- AUTH --------
CREATE TABLE usuario(
    id SERIAL NOT NULL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);
----------------------
