package Vuelos;
/**
autor: Juan Ignacio Cangelosi
**/
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.mysql.jdbc.Statement;

import quick.dbtable.DBTable;

import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Toolkit;

public class VentanaAdmin extends JFrame {

	private JPanel contentPane,panelTabla;
	private JButton btnAplicar;
	private JTextArea txtConsulta;
	private DBTable tablaConsulta;
	private VentanaPrincipal mainFrame;
	private JList listaTablas,listaAtributos;
	private Connection c;
	private String pass;

	//Creacion de la ventana
	public VentanaAdmin(VentanaPrincipal padre, String pass) {
		this.pass=pass; //salvo la contraseña del administrador
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaAdmin.class.getResource("/vuelos/icono.png")));
		setTitle("Administrador");
		mainFrame=padre;											//se guarda a la VentanaPrincipal en caso de necesitar reactivarlo
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 879, 523);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(100, 149, 237));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnAplicar = new JButton("Aplicar");
		btnAplicar.addActionListener(new btnAplicarListener());
		btnAplicar.setBounds(231, 197, 89, 23);
		contentPane.add(btnAplicar);
		
		panelTabla = new JPanel();
		panelTabla.setBounds(10, 229, 532, 167);
		contentPane.add(panelTabla);
		panelTabla.setLayout(null);
		tablaConsulta=new DBTable();
		tablaConsulta.setBounds(0, 0, 534, 167);
		panelTabla.add(tablaConsulta);
		tablaConsulta.setEditable(false);
		
		txtConsulta = new JTextArea();
		txtConsulta.setText("Introduzca su consulta aqui");
		txtConsulta.setBounds(10, 11, 532, 175);
		contentPane.add(txtConsulta);
			
		// se conecta a la base de datos la tabla y se llenan las listas
		conectarBD();
		listaTablas=new JList();
		llenarListaTablas();
		listaTablas.setBounds(563, 32, 137, 421);
		listaTablas.addListSelectionListener(new listaTablasListener());
		contentPane.add(listaTablas);
		
		listaAtributos=new JList();
		listaAtributos.setBounds(723, 32, 130, 421);
		contentPane.add(listaAtributos);
		
		JLabel lblTablas = new JLabel("Tablas");
		lblTablas.setBounds(613, 11, 46, 14);
		contentPane.add(lblTablas);
		
		JLabel lblAtributos = new JLabel("Atributos");
		lblAtributos.setBounds(766, 11, 55, 14);
		contentPane.add(lblAtributos);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setBounds(10, 450, 89, 23);
		contentPane.add(btnVolver);
		btnVolver.addActionListener(new btnVolverListener());
	}
	
	//actualiza la tabla con la consulta
	private void refrescarTabla(){
		//obtengo el texto de la consulta del textBox
		String consulta=txtConsulta.getText();
		
		try
	      {  
			Statement s=(Statement) c.createStatement();
			if(s.execute(consulta)){
	    	  tablaConsulta.setSelectSql(consulta.trim()); //trato de hacer la consulta
	    	  tablaConsulta.createColumnModelFromQuery();    	    
	    	  for (int i = 0; i < tablaConsulta.getColumnCount(); i++)
	    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
	    		 if	 (tablaConsulta.getColumn(i).getType()==Types.TIME)  
	    		 {    		 
	    		  tablaConsulta.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    		 // cambiar el formato en que se muestran los valores de tipo DATE
	    		 if	 (tablaConsulta.getColumn(i).getType()==Types.DATE)
	    		 {
	    		    tablaConsulta.getColumn(i).setDateFormat("dd/MM/YYYY");
	    		 }
	          }   	     	  
	    	  tablaConsulta.refresh();
	       }
	      }catch (SQLException ex)
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
	
	
	
	private void llenarListaTablas(){
		try{
			Statement s=(Statement) c.createStatement();
			s.executeQuery("show tables;");  //hago la consulta para obtener todas las tablas
			ResultSet rs=s.getResultSet();
			DefaultListModel model = new DefaultListModel();
			while(rs.next()){
				model.addElement(rs.getString(1));  //agrego cada tabla del resultSet a mi modelo
			}
			listaTablas.setModel(model);		//agrego el modelo a la Jlist
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
	
	private void llenarListaAtributos(String a){
		try{
			DefaultListModel model = new DefaultListModel();
			Statement s=(Statement) c.createStatement();
			s.executeQuery("Describe "+a+";");  // hago la consulta para obtener todas los atributos de la tabla
			ResultSet rs=s.getResultSet();
			while(rs.next()){
				model.addElement(rs.getString(1));		//agrego cada tabla de resultSet a el modelo
			}	
			listaAtributos.setModel(model);			//cargo el modelo a la JList
			
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
	
	private void conectarBD()
	   {
	         try
	         {
	        	 String driver ="com.mysql.jdbc.Driver";
		         String servidor = "localhost:3306";
		         String baseDatos = "vuelos";
		         String usuario = "admin";
		         String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
	            
	            tablaConsulta.connectDatabase(driver, uriConexion, usuario, pass);
	            c=DriverManager.getConnection(uriConexion, "admin", "admin");
	           
	           
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
	         catch (Exception e)
	         {
	            e.printStackTrace();
	         }
	      
	   }
	 
	 private void cerrarBD(){
			try
		      {
		         if (this.c != null)
		         {
		            c.close();
		            c = null;
		         } 
		         tablaConsulta.close();
		      }
		         catch (SQLException ex)
		         {
		            System.out.println("SQLException: " + ex.getMessage());
		            System.out.println("SQLState: " + ex.getSQLState());
		            System.out.println("VendorError: " + ex.getErrorCode());
		         }
		    
		}
	 
	 private class btnAplicarListener implements ActionListener{
		    public void actionPerformed(ActionEvent e){
		    	refrescarTabla();
		    }
		}
	 
	 private class btnVolverListener implements ActionListener{
		    public void actionPerformed(ActionEvent e){
		    	mainFrame.reActivar();
		    	cerrarBD();
		    	dispose();
		    }
		}
	 
		private class listaTablasListener implements ListSelectionListener{
			public void valueChanged(ListSelectionEvent e){
				String tabla=listaTablas.getSelectedValue().toString();
				llenarListaAtributos(tabla);
			}
		}
}
