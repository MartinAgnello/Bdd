package BatallasMVC;

import java.awt.EventQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BatallasMVC.modelo.*;
import BatallasMVC.vista.*;
import BatallasMVC.controlador.*;

@SuppressWarnings("unused")

//Clase principal
public class Batallas {
	// Se utiliza un logger para ir documentando toda la actividad y errores de la aplicación en un 
	// archivo de texto (ubicado en la carpeta log/log.out) que sirve para debugging. 
	// La configuración del logger se puede encontrar en el archivo: scr/main/resources/log4j.properties 
	private static Logger logger = LoggerFactory.getLogger(Batallas.class);
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Toda actividad se puede ir documentando en el log por medio de: 
					logger.debug("Se inicia la aplicación");
				    // Se crean las instancias de la vista (ventana) 
					VentanaCRUDBatallas ventanaBatallas = new VentanaCRUDBatallasImpl();
					try {
						// Se crea el modelo
						ModeloCRUDBatallas modeloBatallas= new ModeloCRUDBatallasImpl();
						// Se crea el controlador que vincula la vista ventanaBatallas con el modelo modeloBatallas 
						ControladorCRUDBatallas controladorBatallas= new ControladorCRUDBatallasImpl(ventanaBatallas, modeloBatallas);								
					}
					catch (Exception e) {
						logger.error(e.getMessage());
						ventanaBatallas.informar("No se pudo establecer conexion con el servidor de la base de datos", "Error");						
					}				
										
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
	
	
}
