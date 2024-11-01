package BatallasMVC.modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Utils.Conexion;

/**
 * Clase genérica para implementar un modelo para acceder a una base datos siguiendo el patrón de diseño MVC
 */
public class ModeloImpl implements Modelo {
	
	private static Logger logger = LoggerFactory.getLogger(ModeloImpl.class);	

	//Propiedad que mantienen la conexion con el servidor, a partir de la cual el modelo recupera datos de la B.D.    
	protected Connection conexion = null;
 
	//Establece una conexión a la base de datos con un usuario y password utilizando el 
    //Método estático getConection(...) de la clase Conexion
	public boolean conectar(String username, String password) throws Exception {
        this.conexion = Conexion.getConnection(username, password);        
    	return (this.conexion != null);	
	}

	//Cierra la conexión con la base de datos
	public void desconectar() {		
		logger.info("Se desconecta de BD.");
		Conexion.closeConnection(this.conexion);		
	}

	//Realiza una consulta de tipo SELECT en el string sql pasado como parámetro a través de 
	//la propiedad conexion y retorna un objeto de tipo ResulSet
	public ResultSet consulta(String sql) throws SQLException 
	{
		logger.info("Se intenta realizar la siguiente consulta {}",sql);
		Statement stmt = conexion.createStatement();			
		ResultSet rs = stmt.executeQuery(sql);
			
		return rs;
	}	
	

	//Realiza la consulta de actualización (UPDATE, INSERT, DELETE) con el string sql pasado como 
	//parámetro a través de la propiedad conexion 
	public void actualizacion (String sql) throws SQLException 
	{
		Statement stmt = this.conexion.createStatement();
		stmt.executeUpdate(sql);
			
		stmt.close();
	}	
}
