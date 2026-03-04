CREATE DATABASE ecommerceauth;

-------- AUTH --------
CREATE TABLE usuario(
    id SERIAL NOT NULL PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role role VARCHAR(15) NOT NULL DEFAULT 'ROLE_USER' CHECK (role IN ('ROLE_USER','ROLE_ADMIN')
);
----------------------
