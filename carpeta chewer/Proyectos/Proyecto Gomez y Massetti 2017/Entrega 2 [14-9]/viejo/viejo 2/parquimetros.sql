DROP DATABASE parquimetros;
DROP USER 'admin'@'localhost';
DROP USER 'venta'@'localhost';
DROP USER 'inspector'@'localhost';

# Creo de la Base de Datos
CREATE DATABASE parquimetros;

# selecciono la base de datos sobre la cual voy a hacer modificaciones
USE parquimetros;

#-------------------------------------------------------------------------
# Creación Tablas para las entidades

CREATE TABLE conductores (
 dni INT(11) NOT NULL, 
 nombre VARCHAR(45) NOT NULL, 
 apellido VARCHAR(45) NOT NULL, 
 direccion VARCHAR(45) NOT NULL, 
 telefono VARCHAR(45) NOT NULL, 
 registro INT(11) NOT NULL, 
 
 CONSTRAINT pk_conductores 
 PRIMARY KEY (dni)
 ) ENGINE=InnoDB;

 
#
CREATE TABLE automoviles (
 patente VARCHAR(6) NOT NULL,
 marca VARCHAR(45) NOT NULL, 
 modelo VARCHAR(45) NOT NULL, 
 color VARCHAR(45) NOT NULL, 
 dni INT(11) NOT NULL, 
 
 CONSTRAINT pk_automoviles 
 PRIMARY KEY (patente),

 FOREIGN KEY (dni) REFERENCES conductores (dni)
 ON DELETE CASCADE ON UPDATE CASCADE
 
) ENGINE=InnoDB;

#
CREATE TABLE tipos_tarjeta (
 tipo VARCHAR(45) NOT NULL,
 descuento REAL(2,2) NOT NULL, # MIRAR ESTO para poner solo dos dijitos
 
 CONSTRAINT pk_tipos_tarjeta
 PRIMARY KEY (tipo)
 ) ENGINE=InnoDB;

#
CREATE TABLE tarjeta (
 id_tarjeta INT(11) UNSIGNED NOT NULL, 
 saldo REAL(5,2) NOT NULL, # MIRAR ESTO para poner solo dos dijitos
 tipo VARCHAR(45) NOT NULL,
 patente VARCHAR(45) NOT NULL,

 CONSTRAINT pk_tarjeta	
 PRIMARY KEY (id_tarjeta),

 FOREIGN KEY (tipo) REFERENCES tipos_tarjeta (tipo)
 ON DELETE CASCADE ON UPDATE CASCADE,
 FOREIGN KEY (patente) REFERENCES automoviles (patente)
 ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

#
CREATE TABLE inspectores ( 
 legajo INT (11) NOT NULL,
 dni INT(11) UNSIGNED NOT NULL, 
 nombre VARCHAR(45) NOT NULL, 
 apellido VARCHAR(45) NOT NULL, 
 password VARCHAR(32) NOT NULL, #agregar hash md5

 CONSTRAINT pk_inspectores	
 PRIMARY KEY (legajo)
) ENGINE=InnoDB;

#
CREATE TABLE ubicaciones (
 calle VARCHAR(45) NOT NULL,
 altura INT(11) UNSIGNED NOT NULL,  
 tarifa REAL (5,2) UNSIGNED NOT NULL,

 CONSTRAINT pk_ubicaciones	
 PRIMARY KEY (calle,altura)
) ENGINE=InnoDB;


#
CREATE TABLE parquimetros (
 id_parq INT(11) UNSIGNED NOT NULL,
 numero INT(11) NOT NULL,
 calle VARCHAR(45) NOT NULL,
 altura INT(11) UNSIGNED NOT NULL,

 CONSTRAINT pk_parquimetros	
 PRIMARY KEY (id_parq),

  FOREIGN KEY (calle,altura) REFERENCES ubicaciones (calle,altura)
  ON DELETE CASCADE ON UPDATE CASCADE
  
) ENGINE=InnoDB;

#
CREATE TABLE accede(
 legajo INT (11) NOT NULL,
 id_parq INT(11) UNSIGNED NOT NULL,
 fecha DATE NOT NULL,
 hora TIME(2) NOT NULL,

 CONSTRAINT pk_accede	
  PRIMARY KEY (id_parq,fecha,hora),
 
  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_parq) REFERENCES parquimetros (id_parq)
  ON DELETE CASCADE ON UPDATE CASCADE,
 
  KEY (legajo)
) ENGINE=InnoDB;


#
CREATE TABLE estacionamientos (
 id_tarjeta INT(11) UNSIGNED NOT NULL, 
 id_parq INT(11) UNSIGNED NOT NULL,
 fecha_ent DATE NOT NULL,
 hora_ent TIME(2) NOT NULL,
 fecha_sal DATE NULL,
 hora_sal TIME(2) NULL,

 CONSTRAINT pk_estacionamientos	
 PRIMARY KEY (id_parq,fecha_ent,hora_ent),

 FOREIGN KEY (id_tarjeta) REFERENCES tarjeta (id_tarjeta)
 ON DELETE CASCADE ON UPDATE CASCADE,
 FOREIGN KEY (id_parq) REFERENCES parquimetros (id_parq)
 ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=InnoDB;


#
CREATE TABLE asociado_con(
 id_asociado_con INT(11) UNSIGNED NOT NULL, 
 legajo INT (11) NOT NULL,
 calle VARCHAR(45) NOT NULL,
 altura INT(11) UNSIGNED NOT NULL,  
 dia VARCHAR(10) NOT NULL,
 turno CHAR(1) NOT NULL,
 

 CONSTRAINT pk_asociado_con	
  PRIMARY KEY (id_asociado_con),
  
  FOREIGN KEY (legajo) REFERENCES inspectores (legajo)
  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (calle,altura) REFERENCES ubicaciones (calle,altura)
  ON DELETE CASCADE ON UPDATE CASCADE
 
) ENGINE=InnoDB;


#
CREATE TABLE multa(
 numero INT(11) NOT NULL,
 fecha DATE NOT NULL,
 hora TIME(2) NOT NULL,
 patente VARCHAR(45) NOT NULL,
 id_asociado_con INT(11) UNSIGNED NOT NULL,
 
 CONSTRAINT pk_multa
  PRIMARY KEY (numero),
 
  FOREIGN KEY (patente) REFERENCES automoviles (patente)
  ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (id_asociado_con) REFERENCES asociado_con (id_asociado_con)
  ON DELETE CASCADE ON UPDATE CASCADE

) ENGINE=InnoDB;



# Creación de usuarios y otorgamiento de privilegios
# Usuario admin:
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON parquimetros.* TO 'admin'@'localhost';


# Usuario venta:
CREATE USER 'venta'@'localhost' IDENTIFIED BY 'venta';
-- poner privilegios


# Usuario inspector:
CREATE USER 'inspector'@'localhost' IDENTIFIED BY 'inspector';
-- poner privilegios



#-------------------------------------------------------------------------
# Creación de vistas 

CREATE VIEW estacionados AS 
SELECT p.calle,p.altura,t.patente 
FROM (parquimetros as p JOIN estacionamientos as e) JOIN tarjeta as t 
WHERE p.id_parq = e.id_parq AND e.fecha_sal is NULL AND e.hora_sal is NULL AND e.id_tarjeta=t.id_tarjeta;
   
   
   
   

