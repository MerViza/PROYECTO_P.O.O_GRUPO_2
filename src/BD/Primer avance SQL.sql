-- 1. Crear la base de datos
CREATE DATABASE IF NOT EXISTS sistema_computel;
USE sistema_computel;

-- 2. Tabla 'usuarios' (extraída de UsuarioDAO.java)
-- Campos: id, usuario, password
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- 3. Tabla 'productos' (extraída de ProductoDAO.java)
-- Campos: id, codigo, nombre, cantidad, precio
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    categoria ENUM('Pieza', 'Equipo') NOT NULL, -- Coincide con p.getCategoria()
    cantidad INT NOT NULL DEFAULT 0,
    precio DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    socket_puerto VARCHAR(50) DEFAULT NULL,      -- Para Piezas
    anios_garantia INT DEFAULT NULL              -- Para Equipos / Laptops
);

-- 4. Inserción de datos de prueba (INSERT)

INSERT INTO productos (codigo, nombre, categoria, cantidad, precio, socket_puerto, anios_garantia) VALUES 
('P-101', 'Procesador AMD Ryzen 7 5700X', 'Pieza', 10, 220.00, 'AM4', NULL),
('P-102', 'Memoria RAM Corsair Vengeance 16GB', 'Pieza', 25, 65.50, 'DDR4', NULL);


-- 5. Inserción de un usuario inicial para pruebas
INSERT INTO usuarios (usuario, password) 
VALUES ('computel_app', 'Computel123');