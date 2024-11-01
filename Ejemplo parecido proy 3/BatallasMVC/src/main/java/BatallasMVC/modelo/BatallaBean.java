package BatallasMVC.modelo;

import java.io.Serializable;

/**
 * Clase para modelar un objeto batalla que representa una tupla de la tabla batallas 
 * en el modelo de objetos  
 *
 */
public interface BatallaBean extends Serializable {

	/**
	 * seters y geters de la propiedades
	 */
	public String getNombre(); 
	public void setNombre(String nombre); 
	public java.util.Date getFecha();
	public void setFecha(java.util.Date fecha); 
	
} 
