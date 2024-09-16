CREATE DATABASE banco;

USE banco;

#------------------------------------------------------
# CREACION DE LAS TABLAS PARA LAS ENTIDADES

CREATE TABLE ciudad (
    cod_postal SMALLINT UNSIGNED NOT NULL,
    nombre VARCHAR(20) NOT NULL,

    CONSTRAINT pk_ciudad /*Se puede escribir algo distinto algo del CONSTRAINT?*/
    PRIMARY KEY (cod_postal)

) ENGINE=InnoDB;



CREATE TABLE sucursal (
    nro_suc SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(20) NOT NULL,
    direccion VARCHAR(20) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    horario VARCHAR(20) NOT NULL,
    cod_postal SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_sucursal
    PRIMARY KEY (nro_suc),

    CONSTRAINT fk_sucursal
    FOREIGN KEY (cod_postal) REFERENCES ciudad (cod_postal)

) ENGINE=InnoDB;



CREATE TABLE empleado (
    legajo SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    apellido VARCHAR(20) NOT NULL,
    nombre VARCHAR(20) NOT NULL,
    tipo_doc VARCHAR(20) NOT NULL,
    nro_doc INT UNSIGNED NOT NULL,
    direccion VARCHAR(20) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    cargo VARCHAR(20) NOT NULL,
    password VARCHAR(32) NOT NULL, /*se debe almacenar de forma segura con la funcion hash MD5*/
    nro_suc SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_empleado
    PRIMARY KEY (legajo),

    CONSTRAINT fk_empleado
    FOREIGN KEY (nro_suc) REFERENCES sucursal (nro_suc)

) ENGINE=InnoDB;

CREATE TABLE cliente (
    nro_cliente SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    apellido VARCHAR(20) NOT NULL,
    nombre VARCHAR(20) NOT NULL,
    tipo_doc VARCHAR(20) NOT NULL,
    nro_doc INT UNSIGNED NOT NULL,
    direccion VARCHAR(20) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    fecha_nac DATE NOT NULL, /*no especifica como se escribe*/

    CONSTRAINT pk_cliente
    PRIMARY KEY (nro_cliente)

) ENGINE=InnoDB;

CREATE TABLE plazo_fijo (
    nro_plazo INT UNSIGNED NOT NULL AUTO_INCREMENT,
    capital DECIMAL(16,2) UNSIGNED NOT NULL,
    fecha_inicio DATE NOT NULL, /*no especifica como se escribe*/
    fecha_fin DATE NOT NULL, /*no especifica como se escribe*/
    tasa_interes DECIMAL(4,2) UNSIGNED NOT NULL,
    interes DECIMAL(16,2) UNSIGNED NOT NULL, /* interes es un atributo derivado*/
    nro_suc SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_plazo_fijo
    PRIMARY KEY (nro_plazo),

    CONSTRAINT fk_plazo_fijo
    FOREIGN KEY (nro_suc) REFERENCES sucursal (nro_suc)

) ENGINE=InnoDB;


CREATE TABLE tasa_plazo_fijo (
    periodo MEDIUMINT UNSIGNED NOT NULL,
    monto_inf DECIMAL(16,2) UNSIGNED NOT NULL,
    monto_sup DECIMAL(16,2) UNSIGNED NOT NULL,
    tasa DECIMAL(4,2) UNSIGNED NOT NULL, /*preguntar*/

    CONSTRAINT pk_tasa_plazo_fijo
    PRIMARY KEY (periodo, monto_inf, monto_sup)

) ENGINE=InnoDB;


CREATE TABLE plazo_cliente (
    nro_plazo INT UNSIGNED NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_plazo_cliente /*preguntar si se pueden poner dos CONSTRAINT*/
    PRIMARY KEY (nro_plazo, nro_cliente),

    CONSTRAINT fk_plazo_cliente
    FOREIGN KEY (nro_plazo) REFERENCES plazo_fijo (nro_plazo),
    FOREIGN KEY (nro_cliente) REFERENCES cliente (nro_cliente)

) ENGINE=InnoDB;


CREATE TABLE prestamo (
    nro_prestamo INT UNSIGNED NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    cant_meses TINYINT UNSIGNED NOT NULL,
    monto DECIMAL(10,2) UNSIGNED NOT NULL,
    tasa_interes DECIMAL(4,2) UNSIGNED NOT NULL,
    interes DECIMAL(9,2) UNSIGNED NOT NULL,
    valor_cuota DECIMAL(9,2) UNSIGNED NOT NULL,
    legajo SMALLINT UNSIGNED NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_prestamo
    PRIMARY KEY (nro_prestamo),

    CONSTRAINT fk_prestamo
    FOREIGN KEY (legajo) REFERENCES empleado (legajo),
    FOREIGN KEY (nro_cliente) REFERENCES cliente (nro_cliente)

) ENGINE=InnoDB;


CREATE TABLE pago (
    nro_prestamo INT UNSIGNED NOT NULL,
    nro_pago TINYINT UNSIGNED NOT NULL,
    fecha_venc DATE NOT NULL,
    fecha_pago DATE,

    CONSTRAINT pk_pago
    PRIMARY KEY (nro_prestamo, nro_pago),

    CONSTRAINT fk_pago
    FOREIGN KEY (nro_prestamo) REFERENCES prestamo (nro_prestamo)

) ENGINE=InnoDB;


CREATE TABLE tasa_prestamo (
    periodo MEDIUMINT UNSIGNED NOT NULL,
    monto_inf DECIMAL(10,2) UNSIGNED NOT NULL,
    monto_sup DECIMAL(10,2) UNSIGNED NOT NULL,
    tasa DECIMAL(4,2) UNSIGNED NOT NULL,

    CONSTRAINT pk_prestamo
    PRIMARY KEY (monto_inf, monto_sup, periodo)

) ENGINE=InnoDB;


CREATE TABLE caja_ahorro (
    nro_ca INT UNSIGNED NOT NULL AUTO_INCREMENT,
    CBU BIGINT UNSIGNED NOT NULL,
    saldo DECIMAL(16,2) UNSIGNED NOT NULL,

    CONSTRAINT pk_caja_ahorro
    PRIMARY KEY (nro_ca)

) ENGINE=InnoDB;


CREATE TABLE cliente_ca (
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_cliente_ca
    PRIMARY KEY (nro_ca, nro_cliente),

    CONSTRAINT fk_cliente_ca
    FOREIGN KEY (nro_cliente) REFERENCES cliente (nro_cliente),
    FOREIGN KEY (nro_ca) REFERENCES caja_ahorro (nro_ca)

) ENGINE=InnoDB;


CREATE TABLE tarjeta (
    nro_tarjeta BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    PIN VARCHAR(32) NOT NULL, /*utilizar hash md5*/
    CVT VARCHAR(32) NOT NULL, /*utilizar hash md5*/
    fecha_venc DATE NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_tarjeta
    PRIMARY KEY (nro_tarjeta),

    CONSTRAINT fk_tarjeta
    FOREIGN KEY (nro_cliente, nro_ca) REFERENCES cliente_ca (nro_cliente, nro_ca)

) ENGINE=InnoDB;


CREATE TABLE caja (
    cod_caja MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT,

    CONSTRAINT pk_caja
    PRIMARY KEY (cod_caja)

) ENGINE=InnoDB;


CREATE TABLE ventanilla (
    cod_caja MEDIUMINT UNSIGNED NOT NULL,
    nro_suc SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_ventanilla
    PRIMARY KEY (cod_caja),

    CONSTRAINT fk_ventanilla
    FOREIGN KEY (cod_caja) REFERENCES caja (cod_caja), /* esta bien?*/
    FOREIGN KEY (nro_suc) REFERENCES sucursal (nro_suc)

) ENGINE=InnoDB;


CREATE TABLE atm (
    cod_caja MEDIUMINT UNSIGNED NOT NULL,
    cod_postal SMALLINT UNSIGNED NOT NULL,
    direccion VARCHAR(20) NOT NULL,

    CONSTRAINT pk_atm
    PRIMARY KEY (cod_caja),

    CONSTRAINT fk_atm
    FOREIGN KEY (cod_caja) REFERENCES caja (cod_caja),
    FOREIGN KEY (cod_postal) REFERENCES ciudad (cod_postal)

) ENGINE=InnoDB;


CREATE TABLE transaccion (
    nro_trans INT UNSIGNED NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora  TIME NOT NULL, /* PREGUNTAR*/
    monto DECIMAL(16,2) UNSIGNED NOT NULL,

    CONSTRAINT pk_transaccion
    PRIMARY KEY (nro_trans)

) ENGINE=InnoDB;


CREATE TABLE debito (
    nro_trans INT UNSIGNED NOT NULL,
    descripcion TINYTEXT,
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_debito
    PRIMARY KEY (nro_trans),

    CONSTRAINT fk_debito
    FOREIGN KEY (nro_trans) REFERENCES transaccion (nro_trans),
    FOREIGN KEY (nro_cliente, nro_ca) REFERENCES cliente_ca (nro_cliente, nro_ca)

) ENGINE=InnoDB;


CREATE TABLE transaccion_por_caja (
    nro_trans INT UNSIGNED NOT NULL,
    cod_caja MEDIUMINT UNSIGNED NOT NULL,

    CONSTRAINT pk_transaccion_por_caja
    PRIMARY KEY (nro_trans),

    CONSTRAINT fk_transaccion_por_caja
    FOREIGN KEY (nro_trans) REFERENCES transaccion (nro_trans),
    FOREIGN KEY (cod_caja) REFERENCES caja (cod_caja)

) ENGINE=InnoDB;


CREATE TABLE deposito (
    nro_trans INT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_deposito
    PRIMARY KEY (nro_trans),

    CONSTRAINT fk_deposito
    FOREIGN KEY (nro_trans) REFERENCES transaccion_por_caja (nro_trans),
    FOREIGN KEY (nro_ca) REFERENCES caja_ahorro (nro_ca)

) ENGINE=InnoDB;


CREATE TABLE extraccion (
    nro_trans INT UNSIGNED NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_extraccion
    PRIMARY KEY (nro_trans),

    CONSTRAINT fk_extraccion
    FOREIGN KEY (nro_cliente, nro_ca) REFERENCES cliente_ca (nro_cliente, nro_ca),
    FOREIGN KEY (nro_trans) REFERENCES transaccion_por_caja (nro_trans)

) ENGINE=InnoDB;


CREATE TABLE transferencia (
    nro_trans INT UNSIGNED NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    origen INT UNSIGNED NOT NULL,
    destino INT UNSIGNED NOT NULL,

    CONSTRAINT pk_transferencia
    PRIMARY KEY (nro_trans),

    CONSTRAINT fk_transferencia
    FOREIGN KEY (nro_trans) REFERENCES transaccion_por_caja (nro_trans),
    FOREIGN KEY (nro_cliente, origen) REFERENCES cliente_ca (nro_cliente, nro_ca),
    FOREIGN KEY (destino) REFERENCES caja_ahorro (nro_ca)

) ENGINE=InnoDB;


#-------------------------------------------------------------------------
# Creación de usuarios y otorgamiento de privilegios


    CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';

    GRANT ALL PRIVILEGES ON banco.* TO 'admin'@'localhost' WITH GRANT OPTION;


    CREATE USER IF NOT EXISTS 'empleado'@'%' IDENTIFIED BY 'empleado';

    -- Privilegios sólo de consulta (SELECT) sobre las tablas Empleado, Sucursal, Tasa Plazo Fijo y Tasa Prestamo
    GRANT SELECT ON banco.empleado TO 'empleado'@'%';
    GRANT SELECT ON banco.sucursal TO 'empleado'@'%';
    GRANT SELECT ON banco.tasa_plazo_fijo TO 'empleado'@'%';
    GRANT SELECT ON banco.tasa_prestamo TO 'empleado'@'%';

    -- Privilegios de consulta (SELECT) e inserción (INSERT) sobre las tablas Préstamo, Plazo Fijo, Plazo Cliente, Caja Ahorro y Tarjeta
    GRANT SELECT, INSERT ON banco.prestamo TO 'empleado'@'%';
    GRANT SELECT, INSERT ON banco.plazo_fijo TO 'empleado'@'%';
    GRANT SELECT, INSERT ON banco.plazo_cliente TO 'empleado'@'%';
    GRANT SELECT, INSERT ON banco.caja_ahorro TO 'empleado'@'%';
    GRANT SELECT, INSERT ON banco.tarjeta TO 'empleado'@'%';

    -- Privilegios de consulta (SELECT), inserción (INSERT) y modificación (UPDATE) sobre las tablas Cliente CA, Cliente y Pago
    GRANT SELECT, INSERT, UPDATE ON banco.cliente_ca TO 'empleado'@'%';
    GRANT SELECT, INSERT, UPDATE ON banco.cliente TO 'empleado'@'%';
    GRANT SELECT, INSERT, UPDATE ON banco.pago TO 'empleado'@'%';


    -- Eliminamos el usuario vacio
    DROP USER IF EXISTS ''@'localhost';



    CREATE USER IF NOT EXISTS 'atm'@'%' IDENTIFIED BY 'atm';

    GRANT SELECT ON banco.caja_ahorro TO 'atm'@'%';
    GRANT INSERT ON banco.transaccion TO 'atm'@'%'; /*PREGUNTAR*/

    /*PREGUNTAR POR JOIN*/




















