#Autores: Hernan Pocchiola LU: 109169, Juan Ignacio Cangelosi LU: 107573

# Creo de la Base de Datos
Create DATABASE vuelos;

# selecciono la base de datos sobre la cual voy a hacer modificaciones
USE vuelos;

##################################################################################
#Creacion de las tablas 

CREATE TABLE ubicaciones(
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL, 
	huso SMALLINT(2) SIGNED NOT NULL,
	
	CONSTRAINT pk_ubicaciones
	PRIMARY KEY(pais,estado,ciudad)
	
)ENGINE=InnoDB;

CREATE TABLE clases(
	nombre VARCHAR(45) NOT NULL,
	porcentaje DECIMAL(2,2) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_clases
	PRIMARY KEY (nombre)
	
)ENGINE=InnoDB;

CREATE TABLE modelos_avion(
	modelo VARCHAR(45) NOT NULL,
	fabricante VARCHAR(45) NOT NULL,
	cabinas INT UNSIGNED NOT NULL,
	cant_asientos INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_modelos_avion
	PRIMARY KEY(modelo)
	
)ENGINE=InnoDB;

CREATE TABLE pasajeros(
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	nacionalidad VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_pasajeros
	PRIMARY KEY (doc_tipo,doc_nro)
	
)ENGINE=InnoDB;

CREATE TABLE empleados(
	legajo INT UNSIGNED NOT NULL, 
	password VARCHAR(32) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_empleados
	PRIMARY KEY (legajo)
	
)ENGINE=InnoDB;

CREATE TABLE comodidades(
	codigo INT UNSIGNED NOT NULL,
	descripcion TEXT(45) NOT NULL,
	
	CONSTRAINT pk_comodidades
	PRIMARY KEY (codigo)
)ENGINE=InnoDB;

CREATE TABLE aeropuertos(
	codigo VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_aeropuertos
	PRIMARY KEY(codigo),

	CONSTRAINT pk_aeropuertos_ubicaciones
	FOREIGN KEY(pais,estado,ciudad) REFERENCES ubicaciones(pais,estado,ciudad)
	ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

Create table vuelos_programados(
	numero VARCHAR(45) NOT NULL,
	aeropuerto_salida VARCHAR(45) NOT NULL,
	aeropuerto_llegada VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_vuelos_programados
	PRIMARY KEY (numero),

	CONSTRAINT pk_vuelos_programados_llegada
	FOREIGN KEY (aeropuerto_llegada) REFERENCES aeropuertos(codigo)
	ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT pk_vuelos_programados_salida
	FOREIGN KEY (aeropuerto_salida) REFERENCES aeropuertos(codigo)
	ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE salidas(
	vuelo VARCHAR(45) NOT NULL,
	dia ENUM('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'),
	hora_sale TIME NOT NULL,
	hora_llega TIME NOT NULL,
	modelo_avion VARCHAR(45) NOT NULL,

	CONSTRAINT pk_salidas
	PRIMARY KEY (vuelo, dia),

	CONSTRAINT pk_salidas_vuelo
	FOREIGN KEY (vuelo) REFERENCES vuelos_programados(numero)
	ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT pk_salidas_modelo_avion
	FOREIGN KEY (modelo_avion) REFERENCES modelos_avion(modelo)
	ON DELETE RESTRICT ON UPDATE CASCADE

)ENGINE=InnoDB;

CREATE TABLE instancias_vuelo(
	fecha DATE NOT NULL,
	estado VARCHAR(45),
	vuelo VARCHAR(45) NOT NULL,
	dia ENUM('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa') NOT NULL,
	
	CONSTRAINT pk_instancias_vuelo
	PRIMARY KEY(vuelo,fecha),

	CONSTRAINT pk_instancias_vuelo_salidas
	FOREIGN KEY(vuelo,dia) REFERENCES salidas(vuelo, dia)
	ON DELETE RESTRICT ON UPDATE CASCADE
	
)ENGINE=InnoDB;

CREATE TABLE reservas(
	numero INT UNSIGNED NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	vencimiento DATE NOT NULL,
	estado VARCHAR(45) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT UNSIGNED NOT NULL,
	legajo INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_reservas
	PRIMARY KEY (numero),

	CONSTRAINT pk_reservas_pasajeros
	FOREIGN KEY (doc_tipo,doc_nro) REFERENCES pasajeros(doc_tipo,doc_nro)
	ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT pk_reservas_empleados
	FOREIGN KEY (legajo) REFERENCES empleados(legajo)
	ON DELETE RESTRICT ON UPDATE CASCADE
	
)ENGINE=InnoDB;

CREATE TABLE brinda(
	vuelo VARCHAR(45) NOT NULL,
	dia ENUM ('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa'),
	clase VARCHAR(45) NOT NULL,
	precio DECIMAL(7,2) UNSIGNED NOT NULL,
	cant_asientos INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_brinda
	PRIMARY KEY (vuelo, dia, clase),
	
	CONSTRAINT pk_brinda_salidas
	FOREIGN KEY (vuelo, dia) REFERENCES salidas(vuelo,dia)
	ON DELETE RESTRICT ON UPDATE CASCADE,
	
	CONSTRAINT pk_brinda_clases
	FOREIGN KEY (clase) REFERENCES clases(nombre)
	ON DELETE RESTRICT ON UPDATE CASCADE
	
)ENGINE=InnoDB;

CREATE TABLE posee(
	clase VARCHAR(45) NOT NULL,
	comodidad INT UNSIGNED NOT NULL,
	
	CONSTRAINT pk_posee
	PRIMARY KEY (clase, comodidad),
	
	CONSTRAINT pk_posee_clases
	FOREIGN KEY (clase) REFERENCES clases(nombre)
	ON DELETE CASCADE ON UPDATE CASCADE,
	
	CONSTRAINT pk_posee_comodidades
	FOREIGN KEY (comodidad) REFERENCES comodidades(codigo)
	ON DELETE CASCADE ON UPDATE CASCADE
	
)ENGINE=InnoDB;

CREATE TABLE asientos_reservados(
	clase VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,
	vuelo VARCHAR(45) NOT NULL,
	cantidad INT UNSIGNED NOT NULL,
	
	
	CONSTRAINT pk_asientos_reservados
	PRIMARY KEY (vuelo,fecha,clase),
	
	CONSTRAINT pk_asientos_reservados_clases
	FOREIGN KEY (clase) REFERENCES clases(nombre)
	ON DELETE CASCADE ON UPDATE CASCADE,
	
	CONSTRAINT pk_asientos_reservados_instancias_vuelo
	FOREIGN KEY (vuelo,fecha) REFERENCES instancias_vuelo(vuelo,fecha)
	ON DELETE CASCADE ON UPDATE CASCADE
	
)ENGINE=InnoDB;

CREATE TABLE reserva_vuelo_clase(
	numero INT UNSIGNED NOT NULL,
	vuelo VARCHAR(45) NOT NULL,
	fecha_vuelo DATE NOT NULL,
	clase VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_reserva_vuelo_clase
	PRIMARY KEY(numero, vuelo, fecha_vuelo),

	CONSTRAINT pk_reserva_vuelo_clase_reservas
	FOREIGN KEY(numero) REFERENCES reservas(numero)
	ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT pk_reserva_vuelo_clase_clases
	FOREIGN KEY(clase) REFERENCES clases(nombre)
	ON DELETE RESTRICT ON UPDATE CASCADE,

	CONSTRAINT pk_reserva_vuelo_clase_instancia_vuelo
	FOREIGN KEY(vuelo,fecha_vuelo) REFERENCES instancias_vuelo(vuelo,fecha)
	ON DELETE RESTRICT ON UPDATE CASCADE
)ENGINE=InnoDB;


###############################################################################
## Creacion de vistas

CREATE VIEW informacion_vuelos as
Select vp.numero AS nro_vuelo ,s.modelo_avion , iv.fecha,iv.dia , s.hora_sale ,s.hora_llega, timediff(s.hora_llega,s.hora_sale) as tiempo_estimado,
		aer_sal.codigo as codigo_aer_salida, aer_sal.nombre as nombre_aer_salida, aer_sal.ciudad as ciudad_salida, aer_sal.estado as estado_salida, aer_sal.pais as pais_salida,
		aer_lleg.codigo as codigo_aer_llegada, aer_lleg.nombre as nombre_aer_llegada, aer_lleg.ciudad as ciudad_llegada, aer_lleg.estado as estado_llegada, aer_lleg.pais as pais_llegada,
		b.clase as clase, b.precio as precio_pasaje, round(b.cant_asientos+(c.porcentaje*b.cant_asientos)) as asientos_totales
		
from instancias_vuelo iv,salidas s, vuelos_programados vp,
		aeropuertos aer_sal, aeropuertos aer_lleg,
		brinda b, clases c
		
where iv.vuelo=s.vuelo and iv.dia=s.dia and s.vuelo=vp.numero and aer_sal.codigo=vp.aeropuerto_salida and  aer_lleg.codigo=vp.aeropuerto_llegada and 
		aer_sal.codigo<>aer_lleg.codigo and
		b.vuelo=s.vuelo and b.dia=s.dia and b.clase=c.nombre;

CREATE VIEW vuelos_disponibles AS
	SELECT informacion_vuelos.*,
       		asientos_totales - count(reserva_vuelo_clase.numero) as 
       		asientos_disponibles  

	FROM informacion_vuelos LEFT JOIN reserva_vuelo_clase 
  	   ON informacion_vuelos.nro_vuelo = reserva_vuelo_clase.vuelo AND     
	        informacion_vuelos.fecha = reserva_vuelo_clase.fecha_vuelo  AND
	        informacion_vuelos.clase = reserva_vuelo_clase.clase 
	GROUP BY informacion_vuelos.nro_vuelo, 
		    informacion_vuelos.fecha, 	  	 
 	    	    informacion_vuelos.clase;


				
#######################################################################
######################################################################
			
#Creacion de stored procedures
delimiter !
CREATE PROCEDURE reservar_vuelo_ida (IN doc_tipo varchar(45), IN doc_nro int UNSIGNED, IN legajo int, IN r_vuelo varchar(45), IN r_fecha_vuelo DATE, IN dia_vuelo varchar(2), IN r_clase varchar(45), OUT mensaje varchar(90))
BEGIN
DECLARE estado varchar(20); 
DECLARE numID int;
DECLARE asientos_total int;
DECLARE asientos_reserva int;
DECLARE asientos_disp int;

DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN # Si se produce una SQLEXCEPTION, se retrocede la transacción con ROLLBACK
set mensaje='SQLEXCEPTION!, transaccion abortada';
ROLLBACK;
END;

Start TRANSACTION;
IF EXISTS (SELECT p.doc_tipo FROM pasajeros p WHERE p.doc_tipo=doc_tipo and p.doc_nro=doc_nro ) THEN
      BEGIN 
        IF  EXISTS (SELECT e.legajo FROM empleados e WHERE e.legajo=legajo) THEN
            BEGIN 
                IF EXISTS (SELECT iv.vuelo FROM instancias_vuelo iv WHERE iv.vuelo=r_vuelo) THEN
					BEGIN
						IF (r_fecha_vuelo>DATE_ADD(CURDATE(),INTERVAL 15 DAY)) THEN
						BEGIN 
							SELECT cantidad INTO asientos_reserva FROM asientos_reservados as ar WHERE r_vuelo=ar.vuelo and r_fecha_vuelo=ar.fecha and r_clase=ar.clase for update;
							SELECT asientos_disponibles into asientos_disp from vuelos_disponibles where nro_vuelo=r_vuelo and fecha=r_fecha_vuelo and clase=r_clase and dia=dia_vuelo;
							IF (asientos_disp>0) THEN
							BEGIN
								SELECT cant_asientos INTO asientos_total FROM brinda as dv WHERE r_vuelo=dv.vuelo and r_clase=dv.clase and dv.dia= dia_vuelo;
								IF ((asientos_total-asientos_reserva)<0) THEN
									set estado ="En Espera";
								ELSE 
									set estado = "Confirmada";
								END IF;
					
								INSERT INTO reservas(doc_tipo, doc_nro, legajo, fecha, vencimiento, estado)
								VALUES (doc_tipo,doc_nro,legajo,CURDATE(),DATE_SUB(r_fecha_vuelo, INTERVAL 15 DAY),estado);
								INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
								VALUES (last_insert_ID(),r_vuelo, r_fecha_vuelo, r_clase);
					
								UPDATE asientos_reservados as ar set ar.cantidad = asientos_reserva+1 where ar.vuelo=r_vuelo and ar.fecha=r_fecha_vuelo and ar.clase=r_clase;
								SET mensaje=CONCAT('La operacion se realizo con exito \n El estado es: ',estado);
							END;	
							ELSE set mensaje='No existen asientos disponibles para esa clase';
							END IF;
						END;
						ELSE
							set mensaje="La fecha de vencimiento de reserva no puede superar a la fecha del vuelo";
						END IF;
					END;
                ELSE
                    set mensaje="No existe el vuelo";
                END IF;
            END; 
        ELSE
            set mensaje="No existe el empleado";
		END IF;
		END;
ELSE
        set mensaje="No existe el pasajero";
END IF;
COMMIT;
END;

! 
delimiter ;
				
#Creacion de stored procedures
delimiter !
CREATE PROCEDURE reservar_vuelo_idaYVuelta (IN doc_tipo varchar(45), IN doc_nro int UNSIGNED, IN legajo int, IN r_vueloIda varchar(45),IN r_vueloVuelta varchar(45), 
														 IN r_fecha_vueloIda DATE, IN r_fecha_vueloVuelta DATE, IN dia_vueloIda varchar(2),IN dia_vueloVuelta varchar(2), IN r_claseIda varchar(45),IN r_claseVuelta varchar(45), 
														 OUT mensaje varchar(90))
BEGIN
DECLARE estado varchar(20); 
DECLARE numID int;
DECLARE asientos_total int;
DECLARE asientos_reservaIda int;
DECLARE asientos_reservaVuelta int;
DECLARE asientos_dispIda int;
DECLARE asientos_dispVuelta int;
DECLARE maxID int;

DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN # Si se produce una SQLEXCEPTION, se retrocede la transacción con ROLLBACK
set mensaje='SQLEXCEPTION!, transaccion abortada';
ROLLBACK;
END;

Start TRANSACTION;
IF EXISTS (SELECT p.doc_tipo FROM pasajeros p WHERE p.doc_tipo=doc_tipo and p.doc_nro=doc_nro ) THEN
      BEGIN 
        IF  EXISTS (SELECT e.legajo FROM empleados e WHERE e.legajo=legajo) THEN
            BEGIN 
                IF ((EXISTS (SELECT iv.vuelo FROM instancias_vuelo iv WHERE iv.vuelo=r_vueloIda ))&& (EXISTS (SELECT iv.vuelo FROM instancias_vuelo iv WHERE iv.vuelo=r_vueloVuelta ) ))THEN
					BEGIN
						IF ((r_fecha_vueloIda>DATE_ADD(CURDATE(),INTERVAL 15 DAY))&&(r_fecha_vueloVuelta>DATE_ADD(CURDATE(),INTERVAL 15 DAY)) )THEN
						BEGIN 
							SELECT cantidad INTO asientos_reservaIda FROM asientos_reservados as ar WHERE r_vueloIda=ar.vuelo and r_fecha_vueloIda=ar.fecha and r_claseIda=ar.clase for update;
							SELECT cantidad INTO asientos_reservaVuelta FROM asientos_reservados as ar WHERE r_vueloVuelta=ar.vuelo and r_fecha_vueloVuelta=ar.fecha and r_claseVuelta=ar.clase for update;
							
							
							SELECT asientos_disponibles into asientos_dispIda from vuelos_disponibles where nro_vuelo=r_vueloIda and fecha=r_fecha_vueloIda and clase=r_claseIda and dia=dia_vueloIda;
							SELECT asientos_disponibles into asientos_dispVuelta from vuelos_disponibles where nro_vuelo=r_vueloVuelta and fecha=r_fecha_vueloVuelta and clase=r_claseVuelta and dia=dia_vueloVuelta;
							IF ((asientos_dispIda>0)&& (asientos_dispVuelta>0) )THEN
							BEGIN
								SELECT cant_asientos INTO asientos_total FROM brinda as dv WHERE r_vueloIda=dv.vuelo and r_claseIda=dv.clase and dv.dia= dia_vueloIda;
								IF ((asientos_total-asientos_reservaIda)<0) THEN
									set estado ="En Espera";
								ELSE 
									BEGIN
										SELECT cant_asientos INTO asientos_total FROM brinda as dv WHERE r_vueloVuelta=dv.vuelo and r_claseVuelta=dv.clase and dv.dia= dia_vueloVuelta;
										IF ((asientos_total-asientos_reservaVuelta)<0) THEN
											set estado ="En Espera";
										ELSE 
											set estado = "Confirmada";
										END IF;
									End;
								END IF;
					
								INSERT INTO reservas(doc_tipo, doc_nro, legajo, fecha, vencimiento, estado)
								VALUES (doc_tipo,doc_nro,legajo,CURDATE(),DATE_SUB(r_fecha_vueloIda, INTERVAL 15 DAY),estado);
								set maxID=last_insert_ID();
								INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
								VALUES (maxID,r_vueloIda, r_fecha_vueloIda, r_claseIda);
								INSERT INTO reserva_vuelo_clase(numero, vuelo, fecha_vuelo, clase) 
								VALUES (maxID,r_vueloVuelta, r_fecha_vueloVuelta, r_claseVuelta);
					
								UPDATE asientos_reservados as ar set ar.cantidad = asientos_reservaIda+1 where ar.vuelo=r_vueloIda and ar.fecha=r_fecha_vueloIda and ar.clase=r_claseIda;
								UPDATE asientos_reservados as ar set ar.cantidad = asientos_reservaVuelta+1 where ar.vuelo=r_vueloVuelta and ar.fecha=r_fecha_vueloVuelta and ar.clase=r_claseVuelta;
								SET mensaje=CONCAT('La operacion se realizo con exito \n El estado es:   ',estado);
							END;	
							ELSE set mensaje='No existen asientos disponibles para esa clase';
							END IF;
						END;
						ELSE
							set mensaje="La fecha de vencimiento de reserva no puede superar a la fecha del vuelo";
						END IF;
					END;
                ELSE
                    set mensaje="No existe el vuelo";
                END IF;
            END; 
        ELSE
            set mensaje="No existe el empleado";
		END IF;
		END;
ELSE
        set mensaje="No existe el pasajero";
END IF;
COMMIT;
END;

! 
delimiter ;
				
####################################################################################
## Creacion de usuarios

CREATE USER admin@localhost IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON vuelos. * TO admin@localhost;

CREATE USER 'empleado'@'%' IDENTIFIED BY 'empleado';
GRANT SELECT on  vuelos.* to 'empleado'@'%';
GRANT DELETE, INSERT, UPDATE on vuelos.reservas to 'empleado'@'%';
GRANT DELETE, INSERT, UPDATE on vuelos.pasajeros to 'empleado'@'%';
GRANT DELETE, INSERT, UPDATE on vuelos.reserva_vuelo_clase to 'empleado'@'%';
GRANT EXECUTE ON PROCEDURE vuelos.reservar_vuelo_ida TO 'empleado'@'%';
GRANT EXECUTE ON PROCEDURE vuelos.reservar_vuelo_idaYVuelta TO 'empleado'@'%';
GRANT SELECT ON mysql.proc TO 'empleado'@'%';

CREATE USER 'cliente'@'%' IDENTIFIED BY 'cliente';
GRANT SELECT on vuelos.vuelos_disponibles to 'cliente'@'%';



