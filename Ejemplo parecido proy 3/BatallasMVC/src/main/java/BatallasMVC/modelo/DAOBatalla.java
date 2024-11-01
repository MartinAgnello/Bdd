package BatallasMVC.modelo;

import java.util.ArrayList;

public interface DAOBatalla {
	
	/**
	 * Inserta una nueva tupla en la tabla batallas a partir de un BatallaBean
	 * 
	 * @param batalla BatallaBean con los datos a insertar
	 * @throws Exception 
	 */
	public void insertarBatalla(BatallaBean batalla) throws Exception;
	
	/**
	 * Borra la tupla de la tabla batallas correspondiente al BatallaBean
	 * 
	 * @param batalla BatallaBean
	 * @throws Exception
	 */
	public void eliminarBatalla(BatallaBean batalla) throws Exception;
	
	/**
	 * recupera la/s batalla/s que coinciden con el nombre (LIKE) y la fecha del parámetro batalla
	 * 
	 * @param batalla
	 * @return lista de BatallaBean
	 * @throws Exception
	 */
	public  ArrayList<BatallaBean> recuperarBatallas(BatallaBean batalla) throws Exception;
	
	/**
	 * Recibe un BatallaBean con los datos de la batalla a actualizar y otro bean con los datos a utilizar.
	 * 
	 * @param batallaSeleccionada
	 * @param batallaModificada
	 * @throws Exception
	 */
	public void actualizarBatalla(BatallaBean batallaSeleccionada, BatallaBean batallaModificada)throws Exception;
	

}
