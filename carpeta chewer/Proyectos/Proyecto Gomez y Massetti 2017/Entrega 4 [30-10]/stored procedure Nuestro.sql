DROP PROCEDURE IF EXISTS conectar;

#Creacion de stored procedures
delimiter !
CREATE PROCEDURE conectar (IN id_tarjeta INTEGER, IN id_parq INTEGER)
BEGIN
DECLARE saldo DECIMAL(5,2);
DECLARE tarifa DECIMAL(5, 2);
DECLARE descuento DECIMAL(3,2);
DECLARE minutos DECIMAL(20,2);
DECLARE parq_actual int;
DECLARE saldo_nuevo int;

# Declaro variables locales para recuperar los errores 
DECLARE codigo_SQL  CHAR(5) DEFAULT '00000';	 
DECLARE codigo_MYSQL INT DEFAULT 0;
DECLARE mensaje_error TEXT;

DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
BEGIN #En caso de una excepción SQLEXCEPTION retrocede la transacción y devuelve el código de error especifico de MYSQL (MYSQL_ERRNO), el código de error SQL  (SQLSTATE) y el mensaje de error  
	GET DIAGNOSTICS CONDITION 1 codigo_MYSQL= MYSQL_ERRNO, codigo_SQL= RETURNED_SQLSTATE, mensaje_error= MESSAGE_TEXT;
	SELECT 'SQLEXCEPTION!, Transaccion abortada' AS resultado, codigo_MySQL, codigo_SQL,  mensaje_error;		
	ROLLBACK;
END;

Start TRANSACTION;
	#Verificar que exita el parquimetro o la tarjeta
	IF EXISTS (SELECT p.id_parq FROM parquimetros as p WHERE id_parq=p.id_parq) THEN
		BEGIN
			IF EXISTS (SELECT t.id_tarjeta FROM tarjetas as t WHERE id_tarjeta=t.id_tarjeta) THEN
				BEGIN
					/* desc   = */ SELECT c.descuento INTO descuento FROM tarjetas t NATURAL JOIN tipos_tarjeta as c WHERE id_tarjeta=t.id_tarjeta LOCK IN SHARE MODE;	
					
					IF EXISTS (SELECT * FROM estacionamientos as e WHERE id_tarjeta=e.id_tarjeta AND e.fecha_sal is NULL AND e.hora_sal is NULL ORDER BY fecha_ent,hora_ent DESC LIMIT 1 FOR UPDATE) THEN
						BEGIN #el cliente se va del estacionamiento
							
							#obtenemos el saldo, parq_actual, tarifa, minutos que estuvo y su nuevo saldo
							/* saldo       = */ SELECT t.saldo INTO saldo FROM tarjetas as t WHERE id_tarjeta=t.id_tarjeta FOR UPDATE;
							/* parq_actual = */ SELECT e.id_parq INTO parq_actual FROM estacionamientos as e WHERE e.id_tarjeta = id_tarjeta AND e.fecha_sal IS NULL AND e.hora_sal IS NULL LOCK IN SHARE MODE;
							/* tarifa      = */ SELECT u.tarifa INTO tarifa FROM ubicaciones as u NATURAL JOIN parquimetros as p WHERE parq_actual=p.id_parq;
							/* minutos     = */ SELECT TRUNCATE((TIME_TO_SEC(TIMEDIFF(now(),CONCAT(e.fecha_ent,' ',e.hora_ent)))/60), 2) INTO minutos FROM estacionamientos as e WHERE id_tarjeta=e.id_tarjeta AND parq_actual=e.id_parq AND e.fecha_sal is NULL AND e.hora_sal is NULL;
							/* saldo_nuevo = */	SET saldo_nuevo = TRUNCATE((saldo-(minutos*tarifa*(1-descuento))),2);					
							
							#actualizar para cerra estacionamiento, primero agrego salida, despues modifico el saldo
							UPDATE estacionamientos as e SET e.fecha_sal=now(), e.hora_sal=now() WHERE id_tarjeta=e.id_tarjeta AND parq_actual=e.id_parq AND e.fecha_sal is NULL AND e.hora_sal is NULL; 
							
							#Resultado: verificar que el saldo sea mayor a '-999' y actulizar saldo en tarjeta
							IF (saldo_nuevo<-999.99) THEN
								BEGIN
									UPDATE tarjetas as t SET t.saldo = '-999.99' WHERE t.id_tarjeta=id_tarjeta;
									SELECT 'Cierre' as Operacion, TRUNCATE (minutos,2) as 'Tiempo Transcurrido (min.)', '-999.99' as 'Saldo Actualizado';
								END;
							ELSE	
								BEGIN
									UPDATE tarjetas AS t SET t.saldo=saldo_nuevo WHERE t.id_tarjeta=id_tarjeta;
									SELECT 'Cierre' AS Operacion, TRUNCATE (minutos,2) as 'Tiempo Transcurrido (MIN)', saldo_nuevo as 'Saldo Actualizado';
								END;
							END IF;	
						END;
					ELSE #el cliete entra al estacionamiento
						BEGIN
							#obetenemos el saldo de la tarjeta
							/* saldo =  */ SELECT t.saldo INTO saldo FROM tarjetas as t WHERE id_tarjeta=t.id_tarjeta;
							/* tarifa = */ SELECT u.tarifa INTO tarifa FROM ubicaciones as u NATURAL JOIN parquimetros p WHERE id_parq = p.id_parq LOCK IN SHARE MODE;

							IF (saldo>0) THEN
								BEGIN
									INSERT INTO estacionamientos (id_tarjeta,id_parq,fecha_ent,hora_ent) VALUES (id_tarjeta, id_parq, now(), now());
									SELECT 'Apertura' as Operacion, 'Exito' as Resultado, TRUNCATE ((saldo/(tarifa*(1-descuento))),2) AS 'Tiempo disponible (min.)';	
								END;
							ELSE
								BEGIN
									SELECT 'Apertura' as Operacion, 'Error' as Resultado, 'Saldo insuficiente' as Motivo;
								END;
							END IF;
							
						END;
					END IF;	
				END;
			ELSE
				BEGIN
					SELECT 'Error' as Resultado, 'id_tarjeta inexistente' as Motivo;
				END;				
			END IF;
		END;
	ELSE
		BEGIN
			SELECT 'Error' as Resultado, 'id_parq inexistente' as Motivo;
		END;
	END IF;
COMMIT;
END;
