delimiter !

#-------------------------------------------------------------------------
# Implementación de los procedimientos

CREATE PROCEDURE conectar(IN id_tarjeta INTEGER , IN id_parq INTEGER)
BEGIN
	DECLARE saldo DECIMAL(5,2);
	DECLARE tarifa DECIMAL(5, 2);
	DECLARE descuento DECIMAL(3,2);
	DECLARE tiempoTranscurrido DECIMAL(6,2);
	
	# Declaro variables locales para recuperar los errores 
	DECLARE codigo_SQL  CHAR(5) DEFAULT '00000';	 
	DECLARE codigo_MYSQL INT DEFAULT 0;
	DECLARE mensaje_error TEXT;
	 
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	BEGIN #En caso de una excepción SQLEXCEPTION retrocede la transacción y devuelve el código de error especifico de MYSQL (MYSQL_ERRNO), el código de error SQL  (SQLSTATE) y el mensaje de error  
		GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO, codigo_SQL= RETURNED_SQLSTATE, mensaje_error= MESSAGE_TEXT;
	    SELECT 'SQLEXCEPTION!, Transaccion abortada' AS resultado, codigo_MySQL, codigo_SQL,  mensaje_error;		
        ROLLBACK;
	END;

	START TRANSACTION;
		IF EXISTS (SELECT t.id_tarjeta FROM tarjetas t WHERE id_tarjeta = t.id_tarjeta) THEN
			BEGIN
				IF EXISTS (SELECT p.id_parq FROM parquimetros p WHERE id_parq = p.id_parq) THEN
					BEGIN											
						SELECT u.tarifa INTO tarifa FROM ubicaciones u NATURAL JOIN parquimetros p WHERE id_parq = p.id_parq;
						SELECT c.descuento INTO descuento FROM tarjetas t NATURAL JOIN tipos_tarjeta c WHERE id_tarjeta = t.id_tarjeta;						
						IF EXISTS (SELECT * FROM estacionamientos e WHERE id_tarjeta = e.id_tarjeta AND id_parq = e.id_parq AND e.fecha_sal IS NULL AND e.hora_sal IS NULL FOR UPDATE) THEN
							BEGIN #Debo cerrar el estacionamiento	
								SELECT t.saldo INTO saldo FROM tarjetas t WHERE id_tarjeta = t.id_tarjeta FOR UPDATE;
								SELECT TRUNCATE ((TIME_TO_SEC (TIMEDIFF (NOW(), CONCAT (e.fecha_ent,' ',e.hora_ent))) / 60), 2) INTO tiempoTranscurrido FROM estacionamientos e WHERE id_tarjeta = e.id_tarjeta  AND id_parq = e.id_parq AND e.fecha_sal IS NULL AND e.hora_sal IS NULL;				
								UPDATE estacionamientos AS e SET e.fecha_sal = curdate() WHERE id_tarjeta = e.id_tarjeta AND id_parq = e.id_parq AND e.fecha_sal IS NULL;
								UPDATE estacionamientos AS e SET e.hora_sal = curtime() WHERE id_tarjeta = e.id_tarjeta AND id_parq = e.id_parq AND e.hora_sal IS NULL;
								UPDATE tarjetas AS t SET t.saldo = TRUNCATE((saldo - (tiempoTranscurrido * tarifa * (1- descuento))), 2) WHERE t.id_tarjeta = id_tarjeta;
								SELECT 'Cierre' AS Operacion, TRUNCATE (tiempoTranscurrido, 2) AS 'Tiempo Transcurrido (MIN)', TRUNCATE((saldo - (tiempoTranscurrido * tarifa * (1 - descuento))), 2) AS 'Saldo Actualizado';								 
							END;
						ELSE
							BEGIN #Debo abrir el estacionamiento
							SELECT t.saldo INTO saldo FROM tarjetas t WHERE id_tarjeta = t.id_tarjeta;
								IF (saldo > 0) THEN
									BEGIN										
										INSERT INTO estacionamientos VALUES (id_tarjeta, id_parq, curdate(), curtime(), NULL, NULL);
										SELECT 'Apertura' AS Operacion, 'Exito' AS Resultado, TRUNCATE ((saldo / (tarifa * (1 - descuento))),2) AS 'Tiempo Disponible (MIN)';										
									END;
								ELSE									
									SELECT 'Apertura' AS Operacion, 'Error' AS Resultado, 'Saldo insuficiente' AS Motivo;
								END IF;
							END;
						END IF;
					END;
				ELSE					
					SELECT 'Error' AS Resultado, 'id_parq inexistente' AS Motivo;
				END IF;
			END;
		ELSE			
			SELECT 'Error' AS Resultado, 'id_tarjeta inexistente' AS Motivo;			
		END IF;
	COMMIT;
END; !

delimiter ;
