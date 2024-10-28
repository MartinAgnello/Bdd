
/* Utilizando la bdd batallas.sql
1)
a)Encontrar el nombre de la clase y el paıs de todas las clases que tengan al menos 10 ca˜nones.
*/

SELECT clase,pais
FROM clases
WHERE nro_caniones >= 10;

/*
b)Encontrar el nombre de todos los barcos puestos en funcionamiento (lanzados) antes de 1918,
pero la columna resultante debera llamarse Barco
*/

SELECT nombre_barco AS Barco
FROM barco_clase natural join barcos
WHERE lanzado < 1918;

/*
c)Encontrar el nombre de los barcos hundidos en batalla, junto con el nombre de la batalla en la
cual resultaron hundidos.
*/

SELECT nombre_barco, nombre_batalla
FROM RESULTADOS
WHERE resultado = "hundido";

/*
d)Encontrar todos los barcos que su nombre sea igual a su clase.
*/

SELECT nombre_barco
FROM BARCO_CLASE
WHERE nombre_barco = clase;

/*
e)Encontrar el nombre de todos los barcos que comienzan con la letra “R”.
*/

SELECT nombre_barco
FROM barcos
WHERE nombre_barco like 'R%';

/*
f)Encontrar el nombre de todos los barcos cuyo nombre consista de 3 o mas palabras (por ej., King George V).
*/

SELECT nombre_barco
FROM barcos
WHERE nombre_barco LIKE '% % %%';

---Otra Solucion:
SELECT nombre_barco
FROM barcos
WHERE LENGTH(nombre_barco) - LENGTH(REPLACE(nombre_barco, ' ', '')) >= 2;

--Primero cuenta la longitud de caracteres del nombre, luego le resta la longitud de caracteres 
--del nombre sin espacios, si habia 2 o mas espacios entonces el nombre del barco tenia 3 o mas palabras.

/*
2)Consultas sobre mas de una relacion
a)Encontrar los barcos cuyo peso sea mayor a 35 toneladas (DESPLAZAMIENTO).
*/
SELECT nombre_barco
FROM barco_clase natural join clases 
WHERE desplazamiento > 35000;

--Otra solucion
SELECT nombre_barco
FROM barco_clase, clases 
WHERE barco_clase.clase = clase.clase AND desplazamiento > 35000;


/*
b)Listar el nombre del barco, desplazamiento y cantidad de ca˜nones, de los barcos que participaron
en la batalla de Guadalcanal.
*/

SELECT nombre_barco, desplazamiento, nro_caniones
FROM barco_clase natural join clases natural join resultados
WHERE nombre_batalla = "Guadalcanal";

--Otra solucion:
SELECT r1.nombre_barco, desplazamiento, nro_caniones
FROM barco_clase bc, clases c1, resultados r1 
WHERE r1.nombre_barco = bc.nombre_barco AND c1.clase = bc.clase AND nombre_batalla = "Guadalcanal";

/*
c)Encontrar aquellos paıses que dispongan tanto de acorazados como de cruceros.
*/

--hago el prod cartesiano
SELECT DISTINCT clase1.pais
FROM clases clase1, clases clase2 
WHERE clase1.pais = clase2.pais AND clase1.tipo = "acorazado" AND clase2.tipo = "crucero";

--opcion alternativa
--joineo por el mismo pais
SELECT DISTINCT clase1.pais
FROM clases clase1 JOIN clases clase2 ON clase1.pais = clase2.pais 
WHERE clase1.pais = clase2.pais AND clase1.tipo = "acorazado" AND clase2.tipo = "crucero";


/*
d)Encontrar aquellos barcos que fueron averiados en alguna batalla pero que luego pelearon en otra.
*/

SELECT DISTINCT r1.nombre_barco
FROM batallas b1 NATURAL JOIN resultados r1, batallas b2 NATURAL JOIN resultados r2 
WHERE r1.nombre_barco = r2.nombre_barco AND r1.resultado = "averiado" AND b1.fecha < b2.fecha;

--Otra solucion: con el JOIN solo hay que especificar donde joineamos
SELECT DISTINCT r1.nombre_barco
FROM resultados r1
JOIN batallas b1 ON r1.nombre_batalla = b1.nombre_batalla
JOIN resultados r2 ON r1.nombre_barco = r2.nombre_barco
JOIN batallas b2 ON r2.nombre_batalla = b2.nombre_batalla
WHERE r1.resultado = 'averiado' AND b1.fecha < b2.fecha;
/*
que b1 y r1 coincidan en nombre de batalla, que r1 y r2 coincidan en nombre, 
y que r2 y b2 coincidan en nombre de batalla
*/

/*
e)Encontrar aquellas batallas en las cuales un mismo paıs participo con al menos tres barcos
*/
--contamos la cant de barcos por pais 
SELECT r1.nombre_batalla, c1.pais
FROM CLASES c1 JOIN BARCO_CLASE bc ON c1.clase = bc.clase 
JOIN RESULTADOS r1 JOIN BARCO_CLASE ON r1.nombre_barco = bc.nombre_barco
GROUP BY r1.nombre_batalla, c1.pais 
HAVING COUNT(DISTINCT bc.nombre_barco) >= 3;

--hecho por nacho:
SELECT r1.nombre_batalla, c1.pais, r1.nombre_barco, 
       r2.nombre_barco, r3.nombre_barco
FROM resultados r1, barco_clase bc1, clases c1,
resultados r2, barco_clase bc2, clases c2,
resultados r3, barco_clase bc3, clases c3
WHERE r1.nombre_batalla = r2.nombre_batalla AND 
      r2.nombre_batalla = r3.nombre_batalla 
     AND r1.nombre_barco < r2.nombre_barco AND r2.nombre_barco < r3.nombre_barco 
     AND r1.nombre_barco < r3.nombre_barco
     AND r1.nombre_barco = bc1.nombre_barco AND bc1.clase = c1.clase
     AND r2.nombre_barco = bc2.nombre_barco AND bc2.clase = c2.clase
     AND r3.nombre_barco = bc3.nombre_barco AND bc3.clase = c3.clase
     AND c1.pais = c2.pais AND c2.pais = c3.pais;

/*
3)a)Encontrar los paıses cuyos barcos tengan el mayor numero de ca˜nones
*/

SELECT DISTINCT c.pais
FROM CLASES c
WHERE c.nro_caniones = (
    SELECT MAX(nro_caniones)
    FROM CLASES
);

/*
b)Encontrar las clases de barcos en las cuales exista al menos un barco de dicha clase hundido en batalla.
*/
SELECT DISTINCT c1.clase
FROM CLASES c1 NATURAL JOIN BARCO_CLASE bc NATURAL JOIN RESULTADOS r1
WHERE r1.resultado = "hundido";

/*
c)Encontrar el nombre de los barcos con ca˜nones de 16 pulgadas de calibre
*/

SELECT nombre_barco
FROM CLASES NATURAL JOIN BARCO_CLASE 
WHERE calibre = 16;

/*
d)Encontrar las batallas en las cuales participaron barcos de la clase Kongo.
*/

SELECT nombre_batalla
FROM RESULTADOS NATURAL JOIN BARCO_CLASE
WHERE clase = "Kongo";



