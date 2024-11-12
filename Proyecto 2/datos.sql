-- Inserción de datos en la tabla ciudad
INSERT INTO ciudad (cod_postal, nombre) VALUES
(1000, 'Ciudad A'),
(2000, 'Ciudad B'),
(3000, 'Ciudad C'),
(4000, 'Ciudad D'),
(5000, 'Ciudad E');

-- Inserción de datos en la tabla sucursal
INSERT INTO sucursal (nro_suc, nombre, direccion, telefono, horario, cod_postal) VALUES
(1,'Sucursal 1', 'Dirección 1', '123456789', '9-18', 1000),
(2,'Sucursal 2', 'Dirección 2', '987654321', '10-17', 2000),
(3,'Sucursal 3', 'Dirección 3', '111222333', '8-16', 4000),
(4,'Sucursal 4', 'Dirección 4', '444555666', '10-19', 5000);

-- Inserción de datos en la tabla empleado
INSERT INTO empleado (legajo, apellido, nombre, tipo_doc, nro_doc, direccion, telefono, cargo, password, nro_suc) VALUES
(2100,  "Kubrick", "Stanley",   "DNI", 19107200, "Calle 2001", "48585818", "Gerente",md5("odisea"), 1),
(1,'Gómez', 'Juan', 'DNI', 12345678, 'Calle 1', '123456789', 'Gerente', MD5('password123'), 1),
(2,'Pérez', 'Ana', 'DNI', 87654321, 'Calle 2', '987654321', 'Cajero', MD5('password123'), 2),
(3,'Fernández', 'Laura', 'DNI', 12398745, 'Calle 5', '333444555', 'Cajero', MD5('password456'), 3),
(4,'Sosa', 'Pablo', 'DNI', 87612354, 'Calle 6', '666777888', 'Gerente', MD5('password789'), 4);

-- Inserción de datos en la tabla cliente
INSERT INTO cliente (nro_cliente, apellido, nombre, tipo_doc, nro_doc, direccion, telefono, fecha_nac) VALUES
(1,'Lopez', 'Maria', 'DNI', 11223344, 'Calle 3', '123456789', '1985-06-15'),
(2,'Martínez', 'Carlos', 'DNI', 55667788, 'Calle 4', '987654321', '1990-12-30'),
(3,'Ramirez', 'Sofia', 'DNI', 33221155, 'Calle 7', '555666777', '1992-03-25'),
(4,'Torres', 'Diego', 'DNI', 44556677, 'Calle 8', '888999000', '1988-11-05'),
(150,  "Hanks", "Tom",'Pasaporte', 20100200, "Calle Soldado Ryan",  "44555111", "1970-10-8"),
(5,'Samuel', 'Jackson', 'DNI', 39115231, 'Calle 123', '888999002', '1995-12-05');


-- Inserción de datos en la tabla prestamo
INSERT INTO prestamo (nro_prestamo, fecha, cant_meses, monto, tasa_interes, interes, valor_cuota, legajo, nro_cliente) VALUES
(10, "2024-1-1", 4,  15000.5, 12.5,  100.5, 1500.5,  2100, 150),	/*KUBRICK (2100) atiende a TOM HANKS (150)*/
(20,'2024-06-01', 12, 5000.00, 6.00, 300.00, 450.00, 1, 1),
(30,'2024-07-01', 24, 10000.00, 5.50, 1100.00, 525.00, 2, 2),
(40,'2024-08-01', 18, 8000.00, 5.75, 460.00, 476.67, 3, 3),
(50,'2024-09-01', 36, 15000.00, 5.25, 787.50, 478.13, 4, 4);

-- Inserción de datos en la tabla pago
INSERT INTO pago (nro_prestamo, nro_pago, fecha_venc, fecha_pago) VALUES
(10, 1, '2024-07-01', '2024-07-01'),	/*prestamo efectuado por KUBRICK (legajo 2100)*/
(10, 2, '2024-08-01', NULL),			/*prestamo efectuado por KUBRICK (legajo 2100)*/
(20, 1, '2024-08-01', '2024-08-01'),
(20, 2, '2024-09-01', '2024-09-01'),
(40, 1, '2023-10-01', NULL),
(40, 2, '2023-11-01', '2024-11-01'),
(40, 3, '2023-12-01', NULL);

-- Inserción de datos en la tabla tasa_prestamo
INSERT INTO tasa_prestamo (periodo, monto_inf, monto_sup, tasa) VALUES
(12, 0.00, 5000.00, 6.00),
(24, 5000.00, 10000.00, 5.50);

-- Inserción de datos en la tabla caja_ahorro
INSERT INTO caja_ahorro (nro_ca, CBU, saldo) VALUES
(51,123456789, 310500.00),
(39,1234567890123456, 1000.00),
(28,6543210987654321, 2000.00),
(27,9876543210123456, 3000.00),
(18,1231231231231234, 5000.00);


-- Inserción de datos en la tabla cliente_ca
INSERT INTO cliente_ca (nro_cliente, nro_ca) VALUES
(1, 51),
(2, 39),
(3, 28),
(150, 27),
(4, 18);

-- Inserción de datos en la tabla tarjeta
INSERT INTO tarjeta (nro_tarjeta, PIN, CVT, fecha_venc, nro_cliente, nro_ca) VALUES
(123456, MD5('casa11AUTO30libros#5CARPA11nunca'), MD5('51477024948831293761622521111509'), '2024-10-28', 150, 27),
(223454,MD5('1234'), MD5('abcd'), '2025-12-31', 1, 51),
(323456,MD5('5678'), MD5('efgh'), '2026-12-31', 2, 39),
(412345,MD5('9101'), MD5('ijkl'), '2027-12-31', 3, 28),
(512345,MD5('1121'), MD5('mnop'), '2028-12-31', 4, 18);


-- Inserción de datos en la tabla caja
INSERT INTO caja (cod_caja) VALUES
(100),
(2),
(3),
(4);

-- Inserción de datos en la tabla transaccion
INSERT INTO transaccion (nro_trans, fecha, hora, monto) VALUES
(1,'2024-09-01', '10:00:00', 100.00),
(2,'2024-09-02', '11:00:00', 200.00),
(3,'2024-09-03', '12:00:00', 150.00),
(4,'2024-09-04', '14:00:00', 250.00);

-- Inserción de datos en la tabla debito
INSERT INTO debito (nro_trans, descripcion, nro_cliente, nro_ca) VALUES
(1, 'Compra en tienda', 1, 51),
(2, 'Compra en línea', 2, 39),
(3, 'Pago de servicios', 3, 28),
(4, 'Retiro de efectivo', 150, 27);

-- Inserción de datos en la tabla transaccion_por_caja
INSERT INTO transaccion_por_caja (nro_trans, cod_caja) VALUES
(1, 100),
(2, 100),
(3, 100),
(4, 100);

-- Inserción de datos en la tabla deposito
INSERT INTO deposito (nro_trans, nro_ca) VALUES
(1, 51),
(2, 39),
(3, 28);

-- Inserción de datos en la tabla extraccion
INSERT INTO extraccion (nro_trans, nro_cliente, nro_ca) VALUES
(4, 1, 51);

-- Inserción de datos en la tabla transferencia
INSERT INTO transferencia (nro_trans, nro_cliente, origen, destino) VALUES
(1, 1, 51, 39),
(2, 2, 39, 51);
