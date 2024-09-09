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
    nro_suc SMALLINT UNSIGNED NOT NULL,
    nombre VARCHAR(20) NOT NULL,
    direccion VARCHAR(20) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    horario VARCHAR(20) NOT NULL,
    cod_postal SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_sucursal
    PRIMARY KEY (nro_suc),

    FOREIGN KEY (cod_postal) REFERENCES ciudad (cod_postal)

) ENGINE=InnoDB;



CREATE TABLE empleado (
    legajo SMALLINT UNSIGNED NOT NULL,
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

    FOREIGN KEY (nro_suc) REFERENCES sucursal (nro_suc)

) ENGINE=InnoDB;

CREATE TABLE cliente (
    nro_cliente SMALLINT UNSIGNED NOT NULL,
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
    nro_plazo INT UNSIGNED NOT NULL,
    capital DECIMAL(3,2) UNSIGNED NOT NULL,
    fecha_inicio DATE NOT NULL, /*no especifica como se escribe*/
    fecha_fin DATE NOT NULL, /*no especifica como se escribe*/
    tasa_interes DECIMAL(3,2) UNSIGNED NOT NULL,
    interes DECIMAL(3,2) UNSIGNED NOT NULL, /* interes es un atributo derivado*/
    nro_suc SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_plazo_fijo
    PRIMARY KEY (nro_plazo),

    FOREIGN KEY (nro_suc) REFERENCES sucursal (nro_suc)

) ENGINE=InnoDB;


CREATE TABLE tasa_plazo_fijo (
    periodo MEDIUMINT UNSIGNED NOT NULL,
    monto_inf DECIMAL(12,2) UNSIGNED NOT NULL,
    monto_sup DECIMAL(12,2) UNSIGNED NOT NULL,
    tasa DECIMAL(3,2) UNSIGNED NOT NULL, /*preguntar*/

    CONSTRAINT pk_tasa_plazo_fijo
    PRIMARY KEY (periodo, monto_inf, monto_sup),

) ENGINE=InnoDB;


CREATE TABLE plazo_cliente (
    nro_plazo INT UNSIGNED NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_plazo_cliente /*preguntar si se pueden poner dos CONSTRAINT*/
    PRIMARY KEY (nro_plazo, nro_cliente),

    FOREIGN KEY (nro_plazo) REFERENCES plazo_fijo (nro_plazo),
    FOREIGN KEY (nro_cliente) REFERENCES cliente (nro_cliente)

) ENGINE=InnoDB;


CREATE TABLE prestamo (
    nro_prestamo INT UNSIGNED NOT NULL,
    fecha DATE NOT NULL,
    cant_meses TINYINT UNSIGNED NOT NULL,
    monto DECIMAL(12,2) UNSIGNED NOT NULL,
    tasa_interes DECIMAL(3,2) UNSIGNED NOT NULL,
    interes DECIMAL(3,2) UNSIGNED NOT NULL,
    valor_cuota DECIMAL(12,2) UNSIGNED NOT NULL,
    legajo SMALLINT UNSIGNED NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_prestamo
    PRIMARY KEY (nro_prestamo),

    FOREIGN KEY (legajo) REFERENCES empleado (legajo),
    FOREIGN KEY (nro_cliente) REFERENCES cliente (nro_cliente)

) ENGINE=InnoDB;


CREATE TABLE pago (
    nro_prestamo INT UNSIGNED NOT NULL,
    nro_pago TINYINT UNSIGNED NOT NULL,
    fecha_venc DATE NOT NULL,
    fecha_pago DATE NOT NULL,

    CONSTRAINT pk_pago
    PRIMARY KEY (nro_prestamo, nro_pago)

) ENGINE=InnoDB;


CREATE TABLE tasa_prestamo (
    periodo MEDIUMINT UNSIGNED NOT NULL,
    monto_inf DECIMAL(12,2) UNSIGNED NOT NULL,
    monto_sup DECIMAL(12,2) UNSIGNED NOT NULL,
    tasa DECIMAL(3,2) UNSIGNED NOT NULL,

    CONSTRAINT pk_prestamo
    PRIMARY KEY (monto_inf, monto_sup, periodo)

) ENGINE=InnoDB;


CREATE TABLE caja_ahorro (
    nro_ca INT UNSIGNED NOT NULL,
    CBU BIGINT UNSIGNED NOT NULL,
    saldo DECIMAL(12,2) UNSIGNED NOT NULL,

    CONSTRAINT pk_caja_ahorro
    PRIMARY KEY (nro_ca)

) ENGINE=InnoDB;


CREATE TABLE cliente_ca (
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_cliente_ca
    PRIMARY KEY (nro_ca, nro_cliente),

    FOREIGN KEY (nro_cliente) REFERENCES cliente (nro_cliente),
    FOREIGN KEY (nro_ca) REFERENCES caja_ahorro (nro_ca)

) ENGINE=InnoDB;


CREATE TABLE tarjeta (
    nro_tarjeta BIGINT UNSIGNED NOT NULL,
    PIN VARCHAR(32) NOT NULL, /*utilizar hash md5*/
    CVT VARCHAR(32) NOT NULL, /*utilizar hash md5*/
    fecha_venc DATE NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_tarjeta
    PRIMARY KEY (nro_tarjeta),

    FOREIGN KEY (nro_cliente, nro_ca) REFERENCES cliente_ca (nro_cliente, nro_ca)

) ENGINE=InnoDB;


CREATE TABLE caja (
    cod_caja MEDIUMINT UNSIGNED NOT NULL,

    CONSTRAINT pk_caja
    PRIMARY KEY (cod_caja)

) ENGINE=InnoDB;


CREATE TABLE ventanilla (
    cod_caja MEDIUMINT UNSIGNED NOT NULL,
    nro_suc SMALLINT UNSIGNED NOT NULL,

    CONSTRAINT pk_ventanilla
    PRIMARY KEY (cod_caja),

    FOREIGN KEY (cod_caja) REFERENCES caja (cod_caja), /* esta bien?*/
    FOREIGN KEY (nro_suc) REFERENCES sucursal (nro_suc)

) ENGINE=InnoDB;


CREATE TABLE atm (
    cod_caja MEDIUMINT UNSIGNED NOT NULL,
    cod_postal SMALLINT UNSIGNED NOT NULL,
    direccion VARCHAR(20) NOT NULL,

    CONSTRAINT pk_atm
    PRIMARY KEY (cod_caja),

    FOREIGN KEY (cod_caja) REFERENCES caja (cod_caja),
    FOREIGN KEY (cod_postal) REFERENCES ciudad (cod_postal)

) ENGINE=InnoDB;


CREATE TABLE transaccion (
    nro_trans INT UNSIGNED NOT NULL,
    fecha DATE NOT NULL,
    hora  TIME NOT NULL, /* PREGUNTAR*/
    monto DECIMAL(12,2) UNSIGNED NOT NULL,

    CONSTRAINT pk_transaccion
    PRIMARY KEY (nro_trans)

) ENGINE=InnoDB;


CREATE TABLE debito (
    nro_trans INT UNSIGNED NOT NULL,
    descripcion VARCHAR(45) NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_debito
    PRIMARY KEY (nro_trans)

    FOREIGN KEY (nro_trans) REFERENCES transaccion (nro_trans),
    FOREIGN KEY (nro_cliente, nro_ca) REFERENCES cliente_ca (nro_cliente, nro_ca)

) ENGINE=InnoDB;


CREATE TABLE transaccion_por_caja (
    nro_trans INT UNSIGNED NOT NULL,
    cod_caja MEDIUMINT UNSIGNED NOT NULL,

    CONSTRAINT pk_transaccion_por_caja
    PRIMARY KEY (nro_trans),

    FOREIGN KEY (nro_trans) REFERENCES transaccion (nro_trans),
    FOREIGN KEY (cod_caja) REFERENCES caja (cod_caja)

) ENGINE=InnoDB;


CREATE TABLE deposito (
    nro_trans INT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_deposito
    PRIMARY KEY (nro_trans),

    FOREIGN KEY (nro_trans) REFERENCES transaccion_por_caja (nro_trans),
    FOREIGN KEY (nro_ca) REFERENCES caja_ahorro (nro_ca)

) ENGINE=InnoDB;


CREATE TABLE extraccion (
    nro_trans INT UNSIGNED NOT NULL,
    nro_cliente SMALLINT UNSIGNED NOT NULL,
    nro_ca INT UNSIGNED NOT NULL,

    CONSTRAINT pk_extraccion
    PRIMARY KEY (nro_trans),

    FOREIGN KEY (nro_trans, nro_ca) REFERENCES cliente_ca (nro_trans, nro_ca),

) ENGINE=InnoDB;


CREATE TABLE transferencia (
    nro_trans INT UNSIGNED NOT NULL,
    nro_cliente INT UNSIGNED NOT NULL,
    origen INT UNSIGNED NOT NULL,
    destino INT UNSIGNED NOT NULL,

    CONSTRAINT pk_transferencia
    PRIMARY KEY (nro_trans),

    FOREIGN KEY (nro_cliente) REFERENCES cliente_ca (nro_cliente),
    FOREIGN KEY (origen) REFERENCES cliente_ca (nro_ca),
    FOREIGN KEY (destino) REFERENCES caja_ahorro (nro_ca)

) ENGINE=InnoDB;




