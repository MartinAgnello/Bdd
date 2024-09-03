
CREATE DATABASE bank;

USE bank;

CREATE TABLE cuentas(
	numero INT UNSIGNED NOT NULL,
	saldo DECIMAL(7,2) NOT NULL,
	PRIMARY KEY(numero)
) ENGINE=InnoDB;

INSERT INTO cuentas VALUES (1,1000);	
INSERT INTO cuentas VALUES (2,2000);	
INSERT INTO cuentas VALUES (3,3000);	


CREATE TABLE movimientos(
	numero INT UNSIGNED AUTO_INCREMENT NOT NULL,
	cuenta INT UNSIGNED NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
	saldo_anterior DECIMAL(7,2) NOT NULL,
	saldo_posterior DECIMAL(7,2) NOT NULL,
    
	CONSTRAINT pk_movimentos
	PRIMARY KEY(numero),

    CONSTRAINT fk_movimentos_cuentas
    FOREIGN KEY(cuenta) REFERENCES cuentas(numero)
) ENGINE=InnoDB;


#..............................................................................
# Ejemplos de stored procedures 


delimiter !
# Defino '!' como delimitador de sentencias  para que sepa cuando termina 
# el procedimiento. Sino interpreta cada ';' dentro del procedimiento
# como el delimitador de un sentencia sql valida. 
# ATENCIÓN: no dejar espacios después de '!', separar con ENTER.

#..............................................................................
# ejemplo 1

create procedure p()
 begin
   select * from cuentas;
 end; !

#..............................................................................
# ejemplo 2: pasaje de parámetros

create procedure q(IN c CHAR(10), IN d DATE)
 begin
   select c as dia, d as fecha;
 end; !

#..............................................................................
# ejemplo 3: pasaje de parámetros
 
create procedure r(IN i INT, OUT o INT)
 begin
    if (i < 0) then
        set o=i; # asignación
    else 
        set o=i+10; 
    end if;
 end; !

#..............................................................................
# ejemplo 4: variables

create procedure porcentaje(IN C INT)
#Calcula el 10% del saldo asociado a la cuenta C
 begin
   # declaración de variables
    declare Saldo_Cuenta, P DECIMAL(7,2);	
	# recupero el saldo de la cuenta C en la variable Saldo_Cuenta
	select saldo into Saldo_Cuenta 	from cuentas where numero = C;
	#OJO! si utilizamos select...into..., la consulta debe devolver una sola fila
	set P = Saldo_Cuenta * 0.1;
	select C as cuenta, Saldo_Cuenta as saldo, P as diez_porciento;
 end; !
 
#..............................................................................
# ejemplo 5: cursores

create procedure inc_saldo(IN monto DECIMAL(7,2))
# incrementa el saldo en el valor de monto, para 
# aquellas cuentas cuyo saldo es menor o igual a $2000 
 begin
   # declaración de variables
   declare fin boolean default false;
   declare nro_cuenta int;
   # declaro un cursor para la consulta que devuelve 
   # las cuentas con saldo menor  o igual a 2000 
   declare C cursor for select numero from cuentas where saldo <= 2000;
   # defino operación a realizar cuando el fetch no encuentre mas filas:
   # set fin=true;
   declare continue handler for not found set fin = true;
   open C; # abro el cursor (ejecuta la consulta asociada)
   fetch C into nro_cuenta; # recupero la primera fila en la variable nro_cuenta 	  
   while not fin do
      update cuentas   # actualizo el saldo de la cuenta
	  set saldo = saldo + monto 
	  where numero = nro_cuenta;	  
          fetch C into nro_cuenta; # recupero la proxima fila en nro_cuenta           
   end while;
   close C; # cierro el cursor
 end; !
 
 

#..............................................................................
# Trigger para registrar la fecha y hora de todos los cambios de saldo 
# que se producen sobre las cuentas. Cada vez que se actualiza el 
# saldo de una cuenta, se almacena en la tabla movimientos el numero de la
# cuenta afectada, fecha, hora, saldo anterior y posterior a la modificación.

CREATE TRIGGER cuentas_update
AFTER UPDATE ON cuentas
FOR EACH ROW
BEGIN
  INSERT INTO movimientos(cuenta, fecha, hora, saldo_anterior, saldo_posterior)
         VALUES(OLD.numero, curdate(), curtime(), OLD.saldo, NEW.saldo); 
 
END; !

#..............................................................................
# transacción para transferir un monto M de la cuentaA a la cuentaB 

CREATE PROCEDURE transferir(IN monto DECIMAL(7,2), IN cuentaA INT,
                            IN cuentaB INT)
                            
  BEGIN   
     # Declaro una variable local saldo_actual	
	 DECLARE saldo_actual_cuentaA DECIMAL(7,2);
     
     # Declaro variables locales para recuperar los errores 
	 DECLARE codigo_SQL  CHAR(5) DEFAULT '00000';	 
	 DECLARE codigo_MYSQL INT DEFAULT 0;
	 DECLARE mensaje_error TEXT;
	 
     # mas info en: http://dev.mysql.com/doc/refman/5.0/en/declare-handler.html
     DECLARE EXIT HANDLER FOR SQLEXCEPTION 	 	 
	  BEGIN #En caso de una excepción SQLEXCEPTION retrocede la transacción y
         	# devuelve el código de error especifico de MYSQL (MYSQL_ERRNO), 
			# el código de error SQL  (SQLSTATE) y el mensaje de error  	  
	    # "GET DIAGNOSTICS" solo disponible a partir de la versión 5.6, 
		# más info en: http://dev.mysql.com/doc/refman/5.6/en/get-diagnostics.html
		GET DIAGNOSTICS CONDITION 1  codigo_MYSQL= MYSQL_ERRNO,  
		                             codigo_SQL= RETURNED_SQLSTATE, 
									 mensaje_error= MESSAGE_TEXT;
	    SELECT 'SQLEXCEPTION!, transacción abortada' AS resultado, 
		        codigo_MySQL, codigo_SQL,  mensaje_error;		
        ROLLBACK;
	  END;		      
         
	 START TRANSACTION;	# Comienza la transacción  
	   IF EXISTS (SELECT * FROM Cuentas WHERE numero=cuentaA) AND
          EXISTS (SELECT * FROM Cuentas WHERE numero=cuentaB)
	   THEN  # verifico que existan ambas cuentas
	      SELECT saldo INTO saldo_actual_cuentaA 
	      FROM cuentas  WHERE numero=cuentaA FOR UPDATE;
          # Recupero el saldo de la cuentaA en la variable saldo_actual_cuentaA.
          # Al utilizar FOR UPDATE se indica que los datos involucrados en la
          # consulta van a ser actualizados luego.
          # De esta forma se obtiene un write_lock sobre estos datos, que se      
          # mantiene hasta que la trans. comete. Esto garantiza que nadie pueda
          # leer ni escribir el saldo de la cuentaA hasta que la trans. comete.      	    
      
	      IF saldo_actual_cuentaA >= monto THEN 	  
	       # si el saldo actual de la cuentaA es suficiente para realizar 
           # la transferencia, entonces actualizo el saldo de ambas cuentas 
	         UPDATE cuentas SET saldo = saldo - monto  WHERE numero=cuentaA;
	         UPDATE cuentas SET saldo = saldo + monto  WHERE numero=cuentaB;
             SELECT 'La transferencia se realizo con exito' AS resultado;               
	      ELSE  
            SELECT 'Saldo insuficiente para realizar la transferencia' 
		        AS resultado;
	      END IF;  
	   ELSE  
            SELECT 'ERROR: Cuenta inexistente' 
		        AS resultado;  
	   END IF;  	 		
		
	 COMMIT;   # Comete la transacción  
 END; !

    
 delimiter ; # restablece ';' como delimitador de sentencias

#.............................................................................. 
# Creo un usuario y le otorgo privilegios para que pueda 
# ejecutar todos los procedimientos de la B.D. bank.

 CREATE USER proc IDENTIFIED BY "proc";
 GRANT EXECUTE ON bank.* TO proc;
 
