package BatallasMVC.controlador;

import java.util.Date;

import BatallasMVC.modelo.BatallaBean;

public interface ControladorCRUDBatallas {
	
	/**
	 * Crea una nueva batalla a partir de su nombre y fecha
	 * @param nombre
	 * @param fecha
	 */
	public void crearBatalla(String nombre, Date fecha);
	
	/**
	 * Busca la/s batalla/s que coinciden con el nombre y la fecha 
	 * @param nombre
	 * @param fecha
	 */
	public void buscarBatallas(String nombre, Date fecha);
	
	/**
	 * Actualiza los datos de la batallaSeleccionada con un nuevo nombre y fecha  
	 * @param batallaSeleccionada
	 * @param nuevoNombre
	 * @param nuevaFecha
	 */
	public void actualizarBatalla(BatallaBean batallaSeleccionada, String nuevoNombre, Date nuevaFecha);
	
	/**
	 * Elimina la batalla correspondiente al nombre y fecha
	 * @param nombre
	 * @param fecha
	 */
	public void eliminarBatalla(String nombre, Date fecha);
	
	/**
	 *  limpia y actualiza la vista 
	 */
	public void limpiarVista();
	
}
