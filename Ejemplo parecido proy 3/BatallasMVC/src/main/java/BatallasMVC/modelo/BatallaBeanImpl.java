package BatallasMVC.modelo;

//import java.util.Date;

//Clase para modelar un objeto batalla que representa una tupla de la tabla batallas 
//en el modelo de objetos  
public class BatallaBeanImpl implements BatallaBean{

	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private java.util.Date fecha;
	
	//setters y getters de las propiedades    
	public String getNombre() {
		return nombre;
	}
    
	public void setNombre(String nombre) {
		this.nombre = nombre;	
	}
    
	public java.util.Date getFecha() {
		return fecha;
	}
    
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}
	
		
	
}
