
-- Creación de la tabla bancos
CREATE TABLE bancos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Clave primaria con auto incremento
    nombre VARCHAR(255),                  -- Nombre del banco
    total_transferencias INT              -- Total de transferencias
);

CREATE TABLE cuentas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Clave primaria con auto incremento
    persona VARCHAR(255),                 -- Nombre de la persona (puede ser nulo)
    saldo DECIMAL(19, 2)                  -- Saldo (puede ser nulo)
);