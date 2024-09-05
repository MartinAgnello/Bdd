SET password FOR root@localhost='bd-dcic';

---------------------------------------------------------------------------------------
#ALTER TABLE

ALTER TABLE barcos drop nombre_barco;

---------------------------------------------------------------------------------------
#DROP TABLE

DROP TABLE barcos;

---------------------------------------------------------------------------------------
#DELETE

DELETE FROM barcos;

SELECT * FROM resultados;

DELETE FROM batallas WHERE nombre_batalla="North Cape";


---------------------------------------------------------------------------------------
#UPDATE

UPDATE barcos set capitan = "SIMONE";

SELECT * FROM barcos;

UPDATE clases set clase="Kongo 1" WHERE clase="Kongo";


SELECT * FROM resultados;

UPDATE barcos set nombre_barco="HMS King George VI"  WHERE nombre_barco="HMS King George V";

---------------------------------------------------------------------------------------
#SELECT

SELECT * FROM barcos;

SELECT * FROM resultados;

SELECT nombre_barco, nombre_batalla 
FROM resultados;
---------------------------------------------------------------------------------------
SELECT nombre_batalla 
FROM resultados;

SELECT distinct nombre_batalla 
FROM resultados;
---------------------------------------------------------------------------------------
SELECT nombre_barco, nombre_batalla 
FROM resultados 
WHERE resultado="hundido";

SELECT nombre_barco 
FROM resultados 
WHERE resultado="hundido" AND nombre_batalla="North Atlantic";
---------------------------------------------------------------------------------------
SELECT * 
FROM   batallas 
WHERE fecha > "1942-01-01";

SELECT nombre_barco 
FROM barcos 
WHERE nombre_barco LIKE 'Re%';

SELECT nombre_barco 
FROM barcos 
WHERE nombre_barco > 'R%';
---------------------------------------------------------------------------------------
SELECT * FROM barcos;

SELECT * FROM resultados;

SELECT * 
FROM barcos, resultados;

SELECT * 
FROM barcos, resultados 
WHERE barcos.nombre_barco=resultados.nombre_barco;

SELECT barcos.nombre_barco, capitan, nombre_batalla, resultado  
FROM barcos, resultados 
WHERE barcos.nombre_barco=resultados.nombre_barco;

SELECT barcos.nombre_barco, capitan, nombre_batalla, resultado  
FROM barcos, resultados 
WHERE barcos.nombre_barco=resultados.nombre_barco AND resultado="hundido";

SELECT b.nombre_barco as barco, capitan, nombre_batalla as batalla, resultado  
FROM barcos as b , resultados as r 
WHERE b.nombre_barco=r.nombre_barco AND resultado="hundido";
