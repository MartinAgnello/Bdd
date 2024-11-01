package BatallasMVC.vista;

import java.util.ArrayList;

import BatallasMVC.controlador.ControladorCRUDBatallas;
import BatallasMVC.modelo.BatallaBean;

public interface VentanaCRUDBatallas {
	
	/**
	 * Setea el controlador encargado de manejar los eventos de la vista (ventana) 
	 * @param controlador
	 */
	public void registrarControlador(ControladorCRUDBatallas controlador);
	
	/**
	 * Limpia el contenido de los campos para ingresar el Nombre y la Fecha de la Batalla
	 */
	public void limpiarCampos();
	
	/**
	 * Muestra una lista de batallas en la tabla (Jtable)  
	 * @param lista
	 */
	public void refrescarTablaBatallas(ArrayList<BatallaBean> lista);

	
	public void mostrarVentana() throws Exception;
	
	public void eliminarVentana();
	
	/**
	 * Muestra una ventana de modal con un mensaje y un título. 
	 * Se utliliza para informar errores al usuario.   
	 * @param mensaje
	 * @param titulo
	 */	
	public void informar(String mensaje, String titulo);
	
}
