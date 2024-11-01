package BatallasMVC.controlador;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BatallasMVC.modelo.*;
import BatallasMVC.vista.*;


public class ControladorCRUDBatallasImpl implements ControladorCRUDBatallas {

	private static Logger logger = LoggerFactory.getLogger(ControladorCRUDBatallas.class);
	
	private ModeloCRUDBatallas modelo;
	private VentanaCRUDBatallas vista;
	
	public ControladorCRUDBatallasImpl(VentanaCRUDBatallas ventana, ModeloCRUDBatallas modelo) { 
		logger.debug("Se crea el controlador.");		
		
		// el constructor del controlador recibe la vista y el modelo que vincula y los 
		// almacena en propiedades privadas 
		this.vista = ventana;
		this.modelo = modelo;
		
		// se registra el controlador en la vista para que la vista pueda asociar sus eventos con métodos del controlador  
		this.vista.registrarControlador(this);
		
		// Carga la tabla de la vista con todas las filas de la tabla batallas
		actualizarVista();	
	}
	
	
		
	// Crea una nueva batalla con los valores recuperados de los campos de texto de la vista (Nombre y Fecha)
	// Inserta la nueva batalla en la base de datos utilizando el método crearBatalla del modelo  
	public void crearBatalla(String nombre, Date fecha) {	
		if (nombre== "" || fecha==null)			
			 vista.informar("Debe ingresar un nombre y una fecha para la nueva batalla", "Entrada incorrecta");
		else {		
			BatallaBean batalla= new BatallaBeanImpl();
			batalla.setNombre(nombre);
			batalla.setFecha(fecha);
			try 
			{
				modelo.crearBatalla(batalla);
				this.actualizarVista();
			}
			catch (Exception e){
				logger.error(e.getMessage());
				vista.informar(e.getMessage(), "Error");
			}			
		}
	}
	
	
	//recupera la/s batalla/s que coinciden con el nombre y la fecha 
	public void buscarBatallas(String nombre, Date fecha) {
		BatallaBean batalla = new BatallaBeanImpl();
		batalla.setNombre(nombre);
		batalla.setFecha(fecha);
		try {
    		vista.refrescarTablaBatallas(modelo.buscarBatallas(batalla));	
    	}
    	catch (Exception e){
    		logger.error(e.getMessage());
    		vista.informar(e.getMessage(), "Error");					
    	}			
	}
	
	
	//Actualiza los datos de la batallaSeleccionada con un nuevo nombre y fecha   
	public void actualizarBatalla(BatallaBean batallaSeleccionada, String nuevoNombre, Date nuevaFecha) {
		BatallaBean batallaModificada = new BatallaBeanImpl();
		batallaModificada.setNombre(nuevoNombre);
		batallaModificada.setFecha(nuevaFecha);
		try {
			modelo.actualizarBatalla(batallaSeleccionada,  batallaModificada);
    		this.actualizarVista();	
    	}
    	catch (Exception e){
    		logger.error(e.getMessage());
    		vista.informar(e.getMessage(), "Error");						
    	}
	}
	
	// Elimina la batalla correspondiente al nombre y fecha
	// utilizando el método eliminarBatalla del modelo
    public void eliminarBatalla(String nombre, Date fecha) {
    	BatallaBean batalla = new BatallaBeanImpl();
		batalla.setNombre(nombre);
		batalla.setFecha(fecha);
		try 
 		{   
			modelo.eliminarBatalla(batalla);
			this.actualizarVista();
		}
		catch (Exception e)	{
			logger.error(e.getMessage());
			vista.informar(e.getMessage(), "Error");
		}
		this.actualizarVista();  			
	}

    // Borra los campos de texto con el Nombre y la fecha de la vista y vuelve a cargar la tabla Batallas 
    // utilizando el método refrescarTablaBatallas() 
    public void limpiarVista() {
       	  vista.limpiarCampos();
    	  this.actualizarVista();      
	}
    
    // Carga y muestra la tabla Batallas en la vista (vista.refrescarTablaBatallas) con todas las batallas
    // que recupera del modelo a través del método modelo.recuperarTablaBatallas()    
    private void actualizarVista() {
    	try {
    		vista.refrescarTablaBatallas(modelo.recuperarTablaBatallas());	
    	}
    	catch (Exception e) {
    		logger.error(e.getMessage());
    		vista.informar(e.getMessage(), "Error");			
    	}
    			
	}

}	
	
