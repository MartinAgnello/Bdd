CREATE DATABASE claseSQL CHARACTER SET latin1 COLLATE latin1_spanish_ci;

USE claseSQL;

CREATE TABLE DIRECTOR(
	cert INT UNSIGNED NOT NULL,
	nombre VARCHAR(40) NOT NULL,
	direccion VARCHAR(40),
	PRIMARY KEY(cert)
) ENGINE=InnoDB;

CREATE TABLE ESTUDIO(
	id INT UNSIGNED AUTO_INCREMENT NOT NULL,	
	nombre VARCHAR(40) NOT NULL,
	direccion VARCHAR(40),
	PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE ACTOR(
	nombre VARCHAR(40) NOT NULL,
	direccion VARCHAR(40),
	sexo ENUM ('M','F'),
	fechanac DATE,
	PRIMARY KEY(nombre)
) ENGINE=InnoDB;

CREATE TABLE PELICULA(
	titulo VARCHAR(100) NOT NULL,
	anio YEAR(4) NOT NULL,
	duracion TIME,
	encolor BOOL,
	estudio INT UNSIGNED,
	director INT UNSIGNED NOT NULL,
	PRIMARY KEY(titulo, anio),
	FOREIGN KEY(director) REFERENCES DIRECTOR(cert),
	FOREIGN KEY(estudio) REFERENCES ESTUDIO(id)
) ENGINE=InnoDB;

CREATE TABLE ACTUA(
	titulo VARCHAR(100) NOT NULL,
	anio YEAR(4) NOT NULL,
	actornombre VARCHAR(40) NOT NULL,
	PRIMARY KEY(titulo,anio,actornombre),
	FOREIGN KEY(titulo,anio) REFERENCES PELICULA(titulo,anio),
	FOREIGN KEY(actornombre) REFERENCES ACTOR(nombre)
) ENGINE=InnoDB;

CREATE TABLE EJNULL(
	i INT UNSIGNED,
	j INT UNSIGNED,
	k CHAR(1)
) ENGINE=InnoDB;

INSERT INTO DIRECTOR VALUES (101,'Ewan McGregor','5th Av. 500');
INSERT INTO DIRECTOR VALUES (102,'Elijah Wood','3rd Av. 300');
INSERT INTO DIRECTOR VALUES (103,'James Cameron','1th Av. 500');
INSERT INTO DIRECTOR VALUES (104,'Peter Jackson','6th Av. 600');
INSERT INTO DIRECTOR VALUES (105,'Gore Verbinski','7th Av. 700');
INSERT INTO DIRECTOR VALUES (106,'Chris Columbus','8th Av. 800');

INSERT INTO ESTUDIO (nombre,direccion) VALUES ('20th Century-Fox','1th Av. 801');
INSERT INTO ESTUDIO (nombre,direccion) VALUES ('New Line Cinema','1th Av. 802');
INSERT INTO ESTUDIO (nombre,direccion) VALUES ('Walt Disney','1th Av. 803');
INSERT INTO ESTUDIO (nombre,direccion) VALUES ('Warner Bros','1th Av. 804');
INSERT INTO ESTUDIO (nombre,direccion) VALUES ('Lucas Films','1th Av. 805');

INSERT INTO ACTOR VALUES ('Leonardo Di Caprio','1st Av. 100','M',NULL);
INSERT INTO ACTOR VALUES ('Elijah Wood','2nd Av. 200','M',NULL);
INSERT INTO ACTOR VALUES ('Johnny Depp','3rd Av. 300','M',NULL);
INSERT INTO ACTOR VALUES ('Daniel Radcliffe','1st Av. 100','M',NULL);
INSERT INTO ACTOR VALUES ('Ewan McGregor','5th Av. 500','M',NULL);

INSERT INTO PELICULA VALUES ('TITANIC',1997,NULL,TRUE,1,103);
INSERT INTO PELICULA SELECT CONVERT('EL SEÑOR DE LOS ANILLOS: EL RETORNO DEL REY' USING latin1),2003,'04:11:00',TRUE,2,104;
INSERT INTO PELICULA VALUES ('Piratas del Caribe: El cofre del hombre muerto',2006,'02:31:00',TRUE,3,105);
INSERT INTO PELICULA VALUES ('Harry Potter y la piedra filosofal',2001,'04:11:00',FALSE,4,106);
INSERT INTO PELICULA SELECT CONVERT('El Señor de los anillos: Las dos torres' USING latin1),2002,'03:43:00',TRUE,2,104; 
INSERT INTO PELICULA VALUES ('La guerra de las galaxias. Episodio I: La amenaza fantasma',1999,'02:13:00',TRUE,5,101); 

INSERT INTO ACTUA VALUES ('TITANIC',1997,'Leonardo Di Caprio');
INSERT INTO ACTUA SELECT CONVERT('EL SEÑOR DE LOS ANILLOS: EL RETORNO DEL REY' USING latin1),2003,'Elijah Wood'; 
INSERT INTO ACTUA VALUES ('Piratas del Caribe: El cofre del hombre muerto',2006,'Johnny Depp');
INSERT INTO ACTUA VALUES ('Harry Potter y la piedra filosofal',2001,'Daniel Radcliffe');
INSERT INTO ACTUA SELECT CONVERT('El Señor de los anillos: Las dos torres' USING latin1),2002,'Elijah Wood'; 
INSERT INTO ACTUA VALUES ('La guerra de las galaxias. Episodio I: La amenaza fantasma',1999,'Ewan McGregor');
 
 INSERT INTO EJNULL VALUES (150,150,'a');
 INSERT INTO EJNULL VALUES (200,200,'b');
 INSERT INTO EJNULL VALUES (350,350,NULL);
 INSERT INTO EJNULL VALUES (NULL,0,NULL);