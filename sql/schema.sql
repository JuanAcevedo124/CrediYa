CREATE DATABASE IF NOT EXISTS crediya_db;
USE crediya_db;

CREATE TABLE IF NOT EXISTS empleados (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL,
  documento VARCHAR(30) UNIQUE NOT NULL,
  rol VARCHAR(30),
  correo VARCHAR(80),
  salario DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS clientes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(80) NOT NULL,
  documento VARCHAR(30) UNIQUE NOT NULL,
  correo VARCHAR(80),
  telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS prestamos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cliente_id INT NOT NULL,
  empleado_id INT NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  interes DECIMAL(5,2) NOT NULL,
  cuotas INT NOT NULL,
  fecha_inicio DATE NOT NULL,
  estado VARCHAR(20) DEFAULT 'pendiente',
  FOREIGN KEY (cliente_id) REFERENCES clientes(id),
  FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

CREATE TABLE IF NOT EXISTS pagos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  prestamo_id INT NOT NULL,
  fecha_pago DATE NOT NULL,
  monto DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (prestamo_id) REFERENCES prestamos(id)
);

-- Datos de ejemplo
INSERT INTO empleados (nombre, documento, rol, correo, salario) VALUES
('Ana García', '1001', 'asesor', 'ana@crediya.com', 2500000),
('Carlos Ruiz', '1002', 'cajero', 'carlos@crediya.com', 2200000);

INSERT INTO clientes (nombre, documento, correo, telefono) VALUES
('Luis Pérez', '2001', 'luis@mail.com', '3001112233'),
('María Torres', '2002', 'maria@mail.com', '3004445566');
