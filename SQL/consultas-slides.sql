# Slide 13

	SELECT titulo, Anio
	FROM ACTUA;

# Slide 14

	SELECT  titulo, Anio, actorNombre
	FROM ACTUA;

	SELECT  *
	FROM ACTUA;
	
# Slide 15

	SELECT actorNombre
	FROM ACTUA;
	
# Slide 16
	
	SELECT DISTINCT actorNombre
	FROM Actua;	

# Slide 17

	SELECT DISTINCT actorNombre
	FROM ACTUA
	WHERE Anio > 2000;

# Slide 19

	SELECT titulo
	FROM Pelicula
	WHERE anio > 1970 AND NOT encolor;
	
	SELECT titulo, encolor, duracion
	FROM Pelicula
	WHERE (anio > 1970 OR duracion < 90) AND estudio = 2;

# Slide 22

	SELECT titulo, Anio
	FROM ACTUA
	WHERE titulo LIKE '%anillos%';

	SELECT titulo, Anio
	FROM ACTUA
	WHERE titulo LIKE BINARY '%anillos%';
	
# Slide 24

	SELECT titulo, actornombre
	FROM ACTUA
	WHERE 	anio > 2000
	ORDER BY actornombre, titulo DESC;

## Slide 21

	SELECT titulo AS 'Título', Anio AS 'Año', actorNombre AS Actor
	FROM ACTUA
	WHERE anio > 2000;
	
## Slide 22

	SELECT titulo, hour(duracion)*60+minute(duracion) AS 'Duración min.'	  
	FROM Pelicula;

	SELECT titulo, 'B&W' AS color
	FROM Pelicula
	WHERE NOT encolor;
	
## Slide 23
	
	SELECT titulo, nombre AS director
	FROM pelicula, director;
	
## Slide 24

	SELECT DISTINCT nombre as 'Nombre Director'
	FROM PELICULA, DIRECTOR
	WHERE titulo LIKE '%anillos%' AND director = cert;

## Slide 25

	SELECT DISTINCT nombre, nombre
	FROM ACTOR, DIRECTOR
	WHERE direccion = direccion;

	SELECT DISTINCT ACTOR.nombre, DIRECTOR.nombre
	FROM ACTOR, DIRECTOR
	WHERE ACTOR.direccion = DIRECTOR.direccion;

	SELECT DISTINCT ACTOR.nombre, DIRECTOR.nombre
	FROM ACTOR, DIRECTOR
	WHERE ACTOR.direccion = DIRECTOR.direccion 
		AND ACTOR.nombre <> DIRECTOR.nombre ;
	
## Slide 26

	SELECT Star1.nombre, Star2.nombre
	FROM ACTOR Star1, ACTOR Star2
	WHERE Star1.direccion = Star2.direccion;
	
	SELECT Star1.nombre, Star2.nombre
	FROM ACTOR Star1, ACTOR Star2
	WHERE Star1.direccion = Star2.direccion AND Star1.nombre <> Star2.nombre;

	SELECT DISTINCT Star1.nombre, Star2.nombre
	FROM ACTOR Star1, ACTOR Star2
	WHERE Star1.direccion = Star2.direccion AND Star1.nombre <> Star2.nombre;
	
	SELECT Star1.nombre, Star2.nombre
	FROM ACTOR Star1, ACTOR Star2
	WHERE Star1.direccion = Star2.direccion AND Star1.nombre < Star2.nombre;
	
## Slide 32

	SELECT DISTINCT nombre
	FROM PELICULA, DIRECTOR
	WHERE titulo LIKE '%anillos%' AND director = cert;
	
	SELECT nombre
	FROM DIRECTOR
	WHERE cert = (	SELECT DISTINCT director
					FROM PELICULA
					WHERE titulo LIKE '%anillos%' );
		
## Slide 35

	SELECT nombre
	FROM DIRECTOR
	WHERE cert IN (	SELECT DISTINCT director
					FROM PELICULA
					WHERE (titulo, anio) IN  (	SELECT titulo, anio
												FROM ACTUA
												WHERE actorNombre = 'Johnny Depp' )
					);

## Slide 36

	SELECT nombre
	FROM DIRECTOR D, (	SELECT DISTINCT director
						FROM PELICULA P, ACTUA A
						WHERE P.titulo = A.titulo AND  P.anio = A.anio AND actorNombre = 'Johnny Depp' 
					)	DIR
	WHERE D.cert = DIR.director;

## Slide 39

	SELECT DISTINCT director
	FROM PELICULA P JOIN ACTUA A ON P.titulo = A.titulo AND P.anio = A.anio 
	WHERE actorNombre = 'Johnny Depp';

	SELECT *
	FROM PELICULA P JOIN ACTUA A ON P.titulo = A.titulo AND P.anio = A.anio 
	WHERE actorNombre = 'Johnny Depp'\G


## Slide 40

	SELECT * 
	FROM ACTOR NATURAL JOIN DIRECTOR;

## Slide 45

	SELECT AVG(duracion)
	FROM PELICULA
	WHERE encolor;
	
	#las funciones sec_to_time y time_to_sec no son operadores de agregación y por lo tanto pueden anidarse
	SELECT sec_to_time(AVG(time_to_sec(duracion)))  
	FROM PELICULA
	WHERE encolor;	

	SELECT COUNT(*)
	FROM ACTUA;	

	SELECT COUNT(DISTINCT actornombre)
	FROM ACTUA;
	
## Slide 46

	SELECT estudio, AVG(duracion), MAX(anio) - MIN(anio)
	FROM PELICULA
	GROUP BY estudio;

## Slide 49

	SELECT nombre, SUM(duracion)
	FROM DIRECTOR, PELICULA
	WHERE cert = director
	GROUP BY nombre
	HAVING MIN(anio) < 2000;

## Slide 56

	SELECT titulo, duracion
	FROM PELICULA;

	SELECT titulo, duracion
	FROM PELICULA
	WHERE duracion <= “03:00:00” OR duracion > “03:00:00”;
	
## Slide 57

	SELECT * FROM EJNULL;
	
	SELECT COUNT(*) FROM EJNULL;

	SELECT COUNT(i) FROM EJNULL;

	SELECT AVG(i) FROM EJNULL;

	SELECT AVG(j) FROM EJNULL;
	
## Slide 58

	SELECT k, COUNT(k), COUNT(*), AVG(i), AVG(j)
	FROM EJNULL
	GROUP BY k;
	
# slide 64

	INSERT INTO ACTOR
	VALUES ("Leonard Nimoy", "6th Av. 600", "M",NULL);

	SELECT * FROM ACTOR;

# slide 65

	INSERT INTO ACTOR(direccion, fechanac, nombre, sexo)
	VALUES ("6th Av. 600",NULL, "Winona Ryder", "F");

	SELECT * FROM ACTOR;

# slide 66

	INSERT INTO PELICULA(anio, titulo, director, estudio)
	VALUES (2003, "Terminator 3: Rise of the Machines",103,3);
	 
	 SELECT * FROM PELICULA WHERE titulo LIKE "%terminator%"\G
 
 # slide 67
	 
			# Directores que fueron / son tambien actores y cantidad de peliculas dirigidas
			
			SELECT nombre, count(*) as cantdir
			FROM  PELICULA JOIN DIRECTOR ON PELICULA.director = DIRECTOR.cert
			WHERE DIRECTOR.nombre IN (SELECT actornombre  FROM ACTUA)
			GROUP BY DIRECTOR.cert;

			#+---------------+---------+
			#| nombre        | cantdir |
			#+---------------+---------+
			#| Ewan McGregor |       1 |
			#+---------------+---------+

			INSERT INTO PELICULA VALUES ('La guerra de las galaxias. Episodio II: El ataque de los Clones' ,2002,'02:12:00',TRUE,5,101); 
			INSERT INTO PELICULA SELECT CONVERT('El Señor de los anillos: La comunidad del anillo' USING latin1),2001,'03:41:00',TRUE,2,102; 

			SELECT nombre, count(*) as cantdir
			FROM  PELICULA JOIN DIRECTOR ON PELICULA.director = DIRECTOR.cert
			WHERE DIRECTOR.nombre IN (SELECT actornombre  FROM ACTUA)
			GROUP BY DIRECTOR.cert;

			#+---------------+---------+
			#| nombre        | cantdir |
			#+---------------+---------+
			#| Ewan McGregor |       2 |
			#| Elijah Wood   |       1 |
			#+---------------+---------+

	CREATE TABLE ACTORDIRECTOR(
		nombre VARCHAR(40) NOT NULL,
		sexo ENUM ('M','F'),
		cantdir INT UNSIGNED,
		PRIMARY KEY(nombre)
	) ENGINE=InnoDB;

	SELECT *  FROM ACTORDIRECTOR;

	INSERT INTO ACTORDIRECTOR(nombre, cantdir)
		
		SELECT nombre, count(*) as cantdir
		FROM  PELICULA JOIN DIRECTOR ON PELICULA.director = DIRECTOR.cert
		WHERE DIRECTOR.nombre IN (SELECT actornombre  FROM ACTUA)
		GROUP BY DIRECTOR.cert;

	SELECT *  FROM ACTORDIRECTOR;

# slide 68	

	DELETE FROM ACTORDIRECTOR;
	
	SHOW TABLES;
	
	SELECT *  FROM ACTORDIRECTOR;
	
	SELECT * FROM  ACTUA;
	
	DELETE FROM ACTUA
	WHERE anio < 2000;
	
	SELECT * FROM  ACTUA;
	
# Slide 70

	UPDATE ACTOR SET fechanac = "1990-01-01";
	
	SELETE * FROM ACTOR;
	
	UPDATE ACTOR SET sexo = 'F'
	WHERE sexo IS NULL;
	
	SELETE * FROM ACTOR;
	
# Slide 75
	
	CREATE TABLE ACTORDIRECTOR2(
		nombre VARCHAR(40) NOT NULL,
		sexo ENUM ('M','F'),
		cantdir INT UNSIGNED,
		PRIMARY KEY(nombre),
		FOREIGN KEY(nombre) REFERENCES ACTUA(actornombre)
	) ENGINE=InnoDB;	