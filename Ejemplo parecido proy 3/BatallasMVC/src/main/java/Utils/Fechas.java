package Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

// Clase con métodos estáticos para convertir diferentes formatos de fechas 
public class Fechas
{

   public static java.util.Date convertirStringADate(String p_fecha) throws ParseException
   {
      java.util.Date retorno = null;
      if (p_fecha != null) {      
          retorno = (new SimpleDateFormat("dd/MM/yyyy")).parse(p_fecha);
      }    
      return retorno;
   }

   public static String convertirDateAString(java.util.Date p_fecha)
   {
      String retorno = null;
      if (p_fecha != null)
      {
         retorno = (new SimpleDateFormat("dd/MM/yyyy")).format(p_fecha);
      }
      return retorno;
   }

   
      
   public static String convertirDateAStringDB(java.util.Date p_fecha)
   {
      String retorno = null;
      if (p_fecha != null)
      {
         retorno = (new SimpleDateFormat("yyyy-MM-dd")).format(p_fecha);
      }
      return retorno;
   }

   public static java.sql.Date convertirDateADateSQL(java.util.Date p_fecha)
   {
      java.sql.Date retorno = null;
      if (p_fecha != null)
      {
         retorno = java.sql.Date.valueOf((new SimpleDateFormat("yyyy-MM-dd")).format(p_fecha));
      }
      return retorno;
   }


   public static java.sql.Date convertirStringADateSQL(String p_fecha) throws ParseException 
   {
      java.sql.Date retorno = null;
      if (p_fecha != null)
      {
         retorno = Fechas.convertirDateADateSQL(Fechas.convertirStringADate(p_fecha));
      }
      return retorno;
   }
  
     
   
   public static boolean validar(String p_fecha) throws ParseException
   {
      if (p_fecha != null)
      {       
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	sdf.setLenient(false);
        	sdf.parse(p_fecha);
            return true;
       }       
      return false;
   }
   
}
