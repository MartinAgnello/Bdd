package BatallasMVC.modelo;

import java.util.ArrayList;
/**
 * Modelo encargado de implementar la operaciones CRUD sobre batallas que serán invocadas 
 * desde el controlador como respuesta a eventos que se producen en la vista
 *
 */
public interface ModeloCRUDBatallas extends Modelo {
	/**
	 * Recupera todas las tuplas de la tabla batallas y las devuelve como una lista de objetos BeanBatalla 
	 * @return
	 * @throws Exception
	 */	   
	public ArrayList<BatallaBean> recuperarTablaBatallas() throws Exception;
	
	/**
	 * Inserta una nueva batalla en la tabla Batallas
	 * @param batalla
	 * @throws Exception
	 */
	public void crearBatalla(BatallaBean batalla) throws Exception;
	
	/**
	 * Elimina una batalla en la tabla Batallas	 
	 * @param batalla
	 * @throws Exception
	 */
	public void eliminarBatalla(BatallaBean batalla) throws Exception;
	
	/**
	 * Recupera la/s batalla/s que coinciden con el nombre y la fecha del parámetro batalla
	 * @param batallaSeleccionada
	 * @param batallaModificada
	 * @throws Exception
	 */
	public void actualizarBatalla(BatallaBean batallaSeleccionada, BatallaBean batallaModificada)throws Exception;
	
	/**
	 * Actualiza los atributos de una batalla
	 * @param batalla
	 * @return
	 * @throws Exception
	 */
	public  ArrayList<BatallaBean> buscarBatallas(BatallaBean batalla) throws Exception;
	
}
