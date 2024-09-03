package Vuelos;
/**
 autor: Juan Ignacio Cangelosi
**/
 import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.mysql.jdbc.Statement;

import quick.dbtable.DBTable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Toolkit;

public class VentanaEmpleado extends JFrame {
	private JTextField txtfechaIda;
	private JTextField txtFechaVuelta;
	private boolean idaYvuelta, seleccion1,seleccion2;
	private JComboBox<String> paisesIda, paisesVuelta;
	private JLabel lblSalida,lblDestino, lblDiaSalida, lblDiaRegreso, labelinfoIda, labelinfoVuelta, lblAsientosDisponibles1, lblAsientosDisponibles;
	private JButton btnSoloIda, btnIdaYVuelta, btnConsultar, btnReservar;
	private JPanel panelIda, panelVuelta, panelAsientosIda, panelAsientosVuelta;
	private VentanaPrincipal mainFrame;
	private VentanaEmpleado estaVentana;
	private VentanaReservas vistaReservas;
	private DBTable tablaVuelosIda, tablaVuelosVuelta, tablaAsientosIda, tablaAsientosVuelta;
	private String legajo;
	private String nroVueloIda,nroVueloVuelta,claseIda,claseVuelta;
	private int filaVueloIda,filaVueloVuelta,filaClaseIda,filaClaseVuelta;
	/**
	 * Create the frame.
	 */
	public VentanaEmpleado(VentanaPrincipal padre, String l) {
		super();
		legajo=l;
		seleccion1=false;
		seleccion2=false;
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaEmpleado.class.getResource("/vuelos/icono.png")));
		mainFrame=padre;
		estaVentana=this;
		tablaVuelosIda= new DBTable();
		tablaVuelosIda.setBounds(0, 0, 507, 182);
		tablaVuelosIda.setEditable(false);
		tablaVuelosIda.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
				    if (e.getClickCount() == 1) {
				      JTable target = (JTable)e.getSource();
				      filaVueloIda = target.getSelectedRow();
				      nroVueloIda= (String) target.getValueAt(filaVueloIda, 0);
				      String horaV= (String) target.getValueAt(filaVueloIda, 2).toString();
				      String horaL=(String) target.getValueAt(filaVueloIda, 4).toString();
				      panelAsientosIda.setVisible(true);
				      lblAsientosDisponibles1.setVisible(true);
				      consultarAsientosIda(nroVueloIda,horaV,horaL);
				      btnReservar.setVisible(false);
				    }
				  }
				});
		tablaVuelosVuelta= new DBTable();
		tablaVuelosVuelta.setBounds(0, 0, 507, 182);
		tablaVuelosVuelta.setEditable(false);
		tablaVuelosVuelta.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
				    if (e.getClickCount() == 1) {
				      JTable target = (JTable)e.getSource();
				      filaVueloVuelta = target.getSelectedRow();
				      nroVueloVuelta= (String) target.getValueAt(filaVueloVuelta, 0);
				      String horaV= (String) target.getValueAt(filaVueloVuelta, 2).toString();
				      String horaL=(String) target.getValueAt(filaVueloVuelta, 4).toString();
				      panelAsientosVuelta.setVisible(true);
				      lblAsientosDisponibles.setVisible(true);
				      consultarAsientosVuelta(nroVueloVuelta,horaV,horaL);
				      btnReservar.setVisible(false);
				    }
				  }
				});
		tablaAsientosIda= new DBTable();
		tablaAsientosIda.setBounds(0, 0, 356, 182);
		tablaAsientosIda.setEditable(false);
		tablaAsientosIda.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
				    if (e.getClickCount() == 1) {
				      seleccion1=true;
				      JTable target = (JTable)e.getSource();
				      filaClaseIda = target.getSelectedRow();
				      claseIda=(String)target.getValueAt(filaClaseIda, 0);
				      if(!idaYvuelta){
				    	  btnReservar.setVisible(true);
				      }
				      else{
				    	  if(seleccion2)
				    		  btnReservar.setVisible(true);
				      }
				    }
				  }
				});
		
		
		tablaAsientosVuelta= new DBTable();
		tablaAsientosVuelta.setBounds(0, 0, 357, 182);
		tablaAsientosVuelta.setEditable(false);
		tablaAsientosVuelta.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
				    if (e.getClickCount() == 1) {
				      seleccion2=true;
				      JTable target = (JTable)e.getSource();
				      filaClaseVuelta = target.getSelectedRow();
				      claseVuelta=(String)target.getValueAt(filaClaseVuelta, 0);
				      if(seleccion1)
				    		  btnReservar.setVisible(true);
				    }
				  }
				});
		
		
		
		conectarBD();
		
		setTitle("Consulta Vuelos");
		idaYvuelta=false;
		setResizable(true);
		getContentPane().setBackground(new Color(100, 149, 237));
		getContentPane().setLayout(null);
		
		panelIda=new JPanel();
		panelIda.setBounds(10, 113, 500, 193);
		getContentPane().add(panelIda);
		panelIda.setLayout(null);
		panelIda.setVisible(false);
		panelIda.setBackground(new Color(100, 149, 237));
		panelIda.add(tablaVuelosIda);
		
		panelVuelta=new JPanel();
		panelVuelta.setBounds(10, 328, 500, 193);
		getContentPane().add(panelVuelta);
		panelVuelta.setLayout(null);
		panelVuelta.setVisible(false);
		panelVuelta.setBackground(new Color(100, 149, 237));
		panelVuelta.add(tablaVuelosVuelta);
		
		panelAsientosVuelta=new JPanel();
		panelAsientosVuelta.setBounds(527, 328, 500, 193);
		getContentPane().add(panelAsientosVuelta);
		panelAsientosVuelta.setLayout(null);
		panelAsientosVuelta.setVisible(false);
		panelAsientosVuelta.setBackground(new Color(100, 149, 237));
		panelAsientosVuelta.add(tablaAsientosVuelta);
		
		paisesIda = new JComboBox<String>();
		paisesIda.setBounds(32, 61, 118, 20);
		getContentPane().add(paisesIda);
		paisesIda.setVisible(false);
		
		paisesVuelta = new JComboBox<String>();
		paisesVuelta.setBounds(160, 61, 128, 20);
		getContentPane().add(paisesVuelta);
		paisesVuelta.setVisible(false);
		
		cargarComboBox();
		
		txtfechaIda = new JTextField();
		txtfechaIda.setBounds(309, 61, 86, 20);
		getContentPane().add(txtfechaIda);
		txtfechaIda.setColumns(10);
		txtfechaIda.setText("dd/mm/aaa");
		txtfechaIda.setVisible(false);
		
		txtFechaVuelta = new JTextField();
		txtFechaVuelta.setBounds(405, 61, 86, 20);
		getContentPane().add(txtFechaVuelta);
		txtFechaVuelta.setColumns(10);
		txtFechaVuelta.setText("dd/mm/aaa");
		txtFechaVuelta.setVisible(false);
		
		btnSoloIda = new JButton("Solo Ida");
		btnSoloIda.setBounds(32, 11, 115, 23);
		getContentPane().add(btnSoloIda);
		btnSoloIda.addActionListener(new IdaListener());
		
		btnIdaYVuelta = new JButton("Ida y Vuelta");
		btnIdaYVuelta.setBounds(167, 11, 137, 23);
		getContentPane().add(btnIdaYVuelta);
		btnIdaYVuelta.addActionListener(new IdaYVueltaListener());
		
		lblSalida = new JLabel("Salida");
		lblSalida.setBounds(32, 45, 62, 14);
		getContentPane().add(lblSalida);
		lblSalida.setVisible(false);
		
		lblDestino = new JLabel("Destino");
		lblDestino.setBounds(165, 45, 62, 14);
		getContentPane().add(lblDestino);
		lblDestino.setVisible(false);
		
		lblDiaSalida = new JLabel("Dia Salida");
		lblDiaSalida.setBounds(309, 45, 62, 14);
		getContentPane().add(lblDiaSalida);
		lblDiaSalida.setVisible(false);
		
		lblDiaRegreso = new JLabel("Dia Regreso");
		lblDiaRegreso.setBounds(405, 45, 86, 14);
		getContentPane().add(lblDiaRegreso);
		lblDiaRegreso.setVisible(false);
		
		btnConsultar = new JButton("Consultar");
		btnConsultar.setBounds(507, 61, 128, 20);
		getContentPane().add(btnConsultar);
		
		panelAsientosIda=new JPanel();
		panelAsientosIda.setBounds(527, 113, 475, 193);
		getContentPane().add(panelAsientosIda);
		panelAsientosIda.setLayout(null);
		panelAsientosIda.setVisible(false);
		panelAsientosIda.setBackground(new Color(100, 149, 237));
		panelAsientosIda.add(tablaAsientosIda);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setBounds(797, 23, 89, 23);
		getContentPane().add(btnVolver);
		
		//creo las labels de las tablas
		
		lblAsientosDisponibles1 = new JLabel("Asientos Disponbiles");
		lblAsientosDisponibles1.setBounds(540, 92, 207, 14);
		getContentPane().add(lblAsientosDisponibles1);
		lblAsientosDisponibles1.setVisible(false);
		
		lblAsientosDisponibles = new JLabel("Asientos Disponibles");
		lblAsientosDisponibles.setBounds(537, 305, 256, 14);
		getContentPane().add(lblAsientosDisponibles);
		lblAsientosDisponibles.setVisible(false);
		
		labelinfoIda = new JLabel("");
		labelinfoIda.setBounds(21, 92, 489, 14);
		getContentPane().add(labelinfoIda);
		labelinfoIda.setVisible(false);
		
		labelinfoVuelta = new JLabel("");
		labelinfoVuelta.setBounds(20, 303, 490, 14);
		getContentPane().add(labelinfoVuelta);
		
		btnReservar = new JButton("Reservar");
		btnReservar.setBounds(704, 60, 89, 23);
		getContentPane().add(btnReservar);
		btnReservar.setVisible(false);
		btnReservar.addActionListener(new ReservarListener());
		
		labelinfoVuelta.setVisible(false);
		
		//creo el boton de consultar
		
		btnConsultar.addActionListener(new ConsultarListener());
		btnConsultar.setVisible(false);
		btnVolver.addActionListener(new btnVolverListener());
		
		//seteo el tamaño de la ventana
		setBounds(100, 100, 912, 571);

	}
	
	private void mostrarSoloIda(){
		panelIda.setVisible(true);
		labelinfoIda.setVisible(true);
    	labelinfoIda.setText("Vuelos de Ida de: "+(String)paisesIda.getSelectedItem() +" a "+(String)paisesVuelta.getSelectedItem() +" En Fecha: "+txtfechaIda.getText());
    	labelinfoVuelta.setVisible(false);
		refrescarIda();
	}
	
	private void mostrarIdaYVuelta(){
		panelVuelta.setVisible(true);
		panelIda.setVisible(true);
		labelinfoIda.setVisible(true);
		labelinfoIda.setText("Vuelos de Ida de: "+(String)paisesIda.getSelectedItem() +" a "+(String)paisesVuelta.getSelectedItem() +" En Fecha: "+txtfechaIda.getText());
    	labelinfoVuelta.setVisible(true);
    	labelinfoVuelta.setText("Vuelos de Ida de: "+(String)paisesVuelta.getSelectedItem() +" a "+(String)paisesIda.getSelectedItem() +" En Fecha: "+txtFechaVuelta.getText());
		refrescarIdaYVuelta();
	}
	
	 private void cargarComboBox(){
		 try{
			 String driver ="com.mysql.jdbc.Driver";
	        	String servidor = "localhost:3306";
	            String baseDatos = "vuelos";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
			 	Connection c=DriverManager.getConnection(uriConexion, "empleado", "empleado");
				Statement s=(Statement) c.createStatement();
				s.executeQuery("Select distinct ciudad from aeropuertos");
				ResultSet rs=s.getResultSet();
				int i=1;
				while(rs.next()){
					String item=rs.getString(i);
					paisesIda.addItem(item); 
					paisesVuelta.addItem(item); 
				}
				
			}catch (SQLException ex)
		      {
		         // en caso de error, se muestra la causa en la consola
		         System.out.println("SQLException: " + ex.getMessage());
		         System.out.println("SQLState: " + ex.getSQLState());
		         System.out.println("VendorError: " + ex.getErrorCode());
		         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
		                                       ex.getMessage() + "\n", 
		                                       "Error al Querer acceder.",
		                                       JOptionPane.ERROR_MESSAGE);
		         
		      }
	 }
	 
	 private void refrescarIda(){
		 String salida=(String)paisesIda.getSelectedItem();
		 String destino=(String)paisesVuelta.getSelectedItem();
		 String fecha=txtfechaIda.getText();
		 
		 fecha=Fechas.convertirDateAStringDB(Fechas.convertirStringADate(fecha));
		 String txtConsulta="select distinct nro_vuelo, nombre_aer_salida,hora_sale, nombre_aer_llegada, hora_llega, modelo_avion,tiempo_estimado from vuelos_disponibles where ciudad_salida='"+salida+"' and ciudad_llegada='"+destino+"'and fecha='"+fecha+"';";
		 // al introducir el distinct se solucionan los vuelos repetidos, pero surge un cartel en consola sobre Unable to automatically create rowcount sql.
		 try	      {    
	    	  tablaVuelosIda.setSelectSql(txtConsulta.trim());
	    	  tablaVuelosIda.createColumnModelFromQuery();    	    
	    	  for (int i = 0; i < tablaVuelosIda.getColumnCount(); i++)
	    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
	    		 if	 (tablaVuelosIda.getColumn(i).getType()==Types.TIME)  {    		 
	    			 tablaVuelosIda.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    		 // cambiar el formato en que se muestran los valores de tipo DATE
	    		 if	 (tablaVuelosIda.getColumn(i).getType()==Types.DATE){
	    		    tablaVuelosIda.getColumn(i).setDateFormat("dd/MM/YYYY");
	    		 }
	          }   	     	  
	    	  tablaVuelosIda.refresh();
	    	  
	       }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al ejecutar la consulta.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
	 }
	 
	 private void refrescarIdaYVuelta(){
		 refrescarIda();
		 String destino=(String)paisesIda.getSelectedItem();
		 String salida=(String)paisesVuelta.getSelectedItem();
		 String fecha=txtFechaVuelta.getText();
		 fecha=Fechas.convertirDateAStringDB(Fechas.convertirStringADate(fecha));
		 String txtConsulta="select distinct nro_vuelo, nombre_aer_salida,hora_sale, nombre_aer_llegada, hora_llega, modelo_avion,tiempo_estimado from vuelos_disponibles where ciudad_salida='"+salida+"' and ciudad_llegada='"+destino+"'and fecha='"+fecha+"';";
		// al introducir el distinct se solucionan los vuelos repetidos, pero surge un cartel en consola sobre Unable to automatically create rowcount sql.
		 try
	      {   
	    	  tablaVuelosVuelta.setSelectSql(txtConsulta.trim());
	    	  tablaVuelosVuelta.createColumnModelFromQuery();    	    
	    	  for (int i = 0; i < tablaVuelosVuelta.getColumnCount(); i++)
	    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
	    		 if	 (tablaVuelosVuelta.getColumn(i).getType()==Types.TIME)  
	    		 {    		 
	    		  tablaVuelosVuelta.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    		 // cambiar el formato en que se muestran los valores de tipo DATE
	    		 if	 (tablaVuelosVuelta.getColumn(i).getType()==Types.DATE)
	    		 {
	    		    tablaVuelosVuelta.getColumn(i).setDateFormat("dd/MM/YYYY");
	    		 }
	          }     	     	  
	    	  tablaVuelosVuelta.refresh();
	       }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al ejecutar la consulta.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
	 }
	 
	 private void consultarAsientosIda(String codigoVuelo, String horaSale, String horaLlega){
		 String fecha=txtfechaIda.getText();
		 fecha=Fechas.convertirDateAStringDB(Fechas.convertirStringADate(fecha));
		 String consulta="select vd.clase , vd.precio_pasaje, vd.asientos_disponibles from vuelos_disponibles vd where vd.nro_vuelo='"+codigoVuelo+"' and ciudad_salida='"+(String)paisesIda.getSelectedItem()+"'and ciudad_llegada='"+(String)paisesVuelta.getSelectedItem()+"' and vd.fecha='"+fecha+"' and vd.hora_sale='"+horaSale+"' and vd.hora_llega='"+horaLlega+"';";
		 try
	      {    
	    	  tablaAsientosIda.setSelectSql(consulta);
	    	  tablaAsientosIda.createColumnModelFromQuery();    	      	     	  
	    	  tablaAsientosIda.refresh();
	       }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al ejecutar la consulta.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
	 }
	 
	 private void consultarAsientosVuelta(String codigoVuelo, String horaSale, String horaLlega){
		 String fecha=txtFechaVuelta.getText();
		 fecha=Fechas.convertirDateAStringDB(Fechas.convertirStringADate(fecha));
		 String consulta="select vd.clase , vd.precio_pasaje, vd.asientos_disponibles from vuelos_disponibles vd where vd.nro_vuelo='"+codigoVuelo+"' and ciudad_salida='"+(String)paisesVuelta.getSelectedItem()+"'and ciudad_llegada='"+(String)paisesIda.getSelectedItem()+"' and vd.fecha='"+fecha+"' and vd.hora_sale='"+horaSale+"' and vd.hora_llega='"+horaLlega+"';";
		 try
	      {    
	    	  tablaAsientosVuelta.setSelectSql(consulta);
	    	  tablaAsientosVuelta.createColumnModelFromQuery();    	     
	    	  tablaAsientosVuelta.refresh();
	       }
	      catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al ejecutar la consulta.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
	 }
	 
	 private boolean validarFecha(String fechaAValidar){
		 boolean valida=true;
		 if(fechaAValidar == null){
			 valida=false;
			 JOptionPane.showMessageDialog(this,
                     "La fecha introducida no puede ser vacia",
                     "Error",
                     JOptionPane.ERROR_MESSAGE);
		}	 
		else{
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);

			try {
				//if not valid, it will throw ParseException
				Date date = sdf.parse(fechaAValidar);

			} catch (ParseException e) {
				valida=false;
				JOptionPane.showMessageDialog(this,
                        "La fecha introducida no es valida, el formato debe ser DD/MM/AAAA",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
			}
		 }
			return valida;
	 }
	 private boolean validarFecha(String fechaAValidar1, String fechaAValidar2){
		 boolean valida=true;
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		 sdf.setLenient(false);
		 Date fecha1=null;
		 Date fecha2=null;

		try {
				//if not valid, it will throw ParseException
				fecha1 = sdf.parse(fechaAValidar1);

		} catch (ParseException e) {
				valida=false;
				JOptionPane.showMessageDialog(this,
                        "La fecha introducida de Ida no es valida, el formato debe ser DD/MM/AAAA",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
			}
		try {
			 //if not valid, it will throw ParseException
			fecha2 = sdf.parse(fechaAValidar2);

			} catch (ParseException e) {
				valida=false;
				JOptionPane.showMessageDialog(this,
                        "La fecha introducida de Vuelta no es valida, el formato debe ser DD/MM/AAAA",
                       	"Error",
                       	JOptionPane.ERROR_MESSAGE);
				}
			if(valida && (!fecha1.before(fecha2) && !fecha1.equals(fecha2))){
				valida=false;
				JOptionPane.showMessageDialog(this,
                        "La fecha de Ida debe ser menor que la de Vuelta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
			}
			return valida;
	 }
	 
	 
	 private void conectarBD()
	   {
	         try
	         {
	            String driver ="com.mysql.jdbc.Driver";
	        	String servidor = "localhost:3306";
	            String baseDatos = "vuelos";
	            String usuario = "empleado";
	            String clave = "empleado";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
	   
	            //establece una conexión con la  B.D. "batallas"  usando directamante una tabla DBTable    
	            tablaVuelosIda.connectDatabase(driver, uriConexion, usuario, clave);
	           tablaVuelosVuelta.connectDatabase(driver, uriConexion, usuario, clave);
	            tablaAsientosIda.connectDatabase(driver, uriConexion, usuario, clave);
	            tablaAsientosVuelta.connectDatabase(driver, uriConexion, usuario, clave);
	           
	         }
	         catch (SQLException ex)
	         {
	             JOptionPane.showMessageDialog(this,
	                                           "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
	                                           "Error",
	                                           JOptionPane.ERROR_MESSAGE);
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	         catch (ClassNotFoundException e)
	         {
	            e.printStackTrace();
	         }
	      
	   }
	 
	 private void cerrarBD(){
		         try
		         {
		            tablaVuelosIda.close();
		            tablaVuelosVuelta.close();
		            tablaAsientosIda.close();
		            tablaAsientosVuelta.close();

		         }
		         catch (SQLException ex)
		         {
		            System.out.println("SQLException: " + ex.getMessage());
		            System.out.println("SQLState: " + ex.getSQLState());
		            System.out.println("VendorError: " + ex.getErrorCode());
		         }
		      }	
	 
	 private class btnVolverListener implements ActionListener{
		    public void actionPerformed(ActionEvent e){
		    	mainFrame.reActivar();
		    	cerrarBD();
		    	dispose();
		    }
		}
	 
	 private class ReservarListener implements ActionListener{
		    public void actionPerformed(ActionEvent e){
		    	cerrarBD();
		    	estaVentana.setVisible(false);
		    	if(idaYvuelta){	
		    		String fechaVueloIda=txtfechaIda.getText();
			    	String fechaVueloVuelta=txtFechaVuelta.getText();
			    	String diaVueloIda=obtenerDia(fechaVueloIda);
			    	String diaVueloVuelta=obtenerDia(fechaVueloVuelta);
		    		vistaReservas= new VentanaReservas(mainFrame,legajo,nroVueloIda,nroVueloVuelta,fechaVueloIda,fechaVueloVuelta,diaVueloIda,diaVueloVuelta,claseIda,claseVuelta);
		    	}
		    	else{
		    		String fechaVueloIda=txtfechaIda.getText();
			    	String diaVueloIda=obtenerDia(fechaVueloIda);
		    		vistaReservas= new VentanaReservas(mainFrame,legajo,nroVueloIda,fechaVueloIda,diaVueloIda,claseIda);
		    	}
		    	
		    	vistaReservas.setVisible(true);
		    	
		    }
		    
		    
		}
	 
		private class ConsultarListener implements ActionListener{
		    public void actionPerformed(ActionEvent e){
		    	if(!idaYvuelta){
		    		if(validarFecha(txtfechaIda.getText()))
		    		mostrarSoloIda();
		    	}
		    	else
		    		if(validarFecha(txtfechaIda.getText(), txtFechaVuelta.getText()))
		    				mostrarIdaYVuelta();
		    }
		}
		
		private class IdaListener implements ActionListener{
		    public void actionPerformed(ActionEvent e){
		    	idaYvuelta=false;
		    	panelAsientosIda.setVisible(false);
		    	paisesIda.setVisible(true);
		    	paisesVuelta.setVisible(true);
		    	lblSalida.setVisible(true);
		    	lblDestino.setVisible(true);
		    	lblDiaSalida.setVisible(true);
		    	lblDiaRegreso.setVisible(false);
		    	txtfechaIda.setVisible(true);
		    	txtFechaVuelta.setVisible(false);
		    	btnConsultar.setVisible(true);
		    	lblAsientosDisponibles.setVisible(false);
		    	lblAsientosDisponibles1.setVisible(false);
		    }
		}
		
		private class IdaYVueltaListener implements ActionListener{
		    public void actionPerformed(ActionEvent e){
		    	idaYvuelta=true;
		    	panelAsientosIda.setVisible(false);
		    	panelAsientosVuelta.setVisible(false);
		    	paisesIda.setVisible(true);
		    	paisesVuelta.setVisible(true);
		    	lblSalida.setVisible(true);
		    	lblDestino.setVisible(true);
		    	lblDiaSalida.setVisible(true);
		    	lblDiaRegreso.setVisible(true);
		    	btnConsultar.setVisible(true);
		    	txtfechaIda.setVisible(true);
		    	txtFechaVuelta.setVisible(true);
		    	lblAsientosDisponibles.setVisible(false);
		    	lblAsientosDisponibles1.setVisible(false);
		    }
		}
		
		private String obtenerDia(String d){
			String dia="Lu";
			Calendar c = Calendar.getInstance();
			Date date=Fechas.convertirStringADate(d);
			c.setTime(date);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			switch(dayOfWeek){
			case 1: dia="Do";
							break;
			case 2: dia="Lu";
			break;
			case 3: dia="Ma";
			break;
			case 4: dia="Mi";
			break;
			case 5: dia="Ju";
			break;
			case 6: dia="Vi";
			break;
			case 7: dia="Sa";
			break;
			}
			return dia;
		}
		
}
