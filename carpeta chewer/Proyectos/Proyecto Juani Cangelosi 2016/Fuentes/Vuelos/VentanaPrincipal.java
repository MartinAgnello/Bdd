package Vuelos;
/**
autor: Juan Ignacio Cangelosi
**/
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Statement;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Toolkit;

public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JPasswordField passwordField;
	private JLabel lblUsername, lblPass;
	private JButton btnEntrar;
	private boolean empleadoOadmin; //vale 0 cuando es admin 1 cuando es empleado;
	private Connection c;
	private VentanaEmpleado vistaEmpleados;
	private VentanaAdmin vistaAdmin;
	private VentanaPrincipal estaVentana;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaPrincipal() {
		estaVentana=this;
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaPrincipal.class.getResource("/vuelos/icono.png")));
		setTitle("Vuelos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 912, 571);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(100, 149, 237));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		conectarBD();
		
		JButton btnAccesoAdmin = new JButton("Acceso Admin");
		btnAccesoAdmin.setBounds(360, 280, 158, 23);
		contentPane.add(btnAccesoAdmin);
		btnAccesoAdmin.addActionListener(new adminlistener());
		
		JButton btnAccesoEmpleado = new JButton("Acceso Empleado");
		btnAccesoEmpleado.setBounds(360, 314, 158, 23);
		contentPane.add(btnAccesoEmpleado);
		btnAccesoEmpleado.addActionListener(new empleadolistener());
		
		txtName = new JTextField();
		txtName.setBounds(292, 397, 121, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(472, 397, 121, 20);
		contentPane.add(passwordField);
		passwordField.setColumns(10);
		
		btnEntrar = new JButton("Entrar");
		btnEntrar.setBounds(404, 439, 89, 23);
		contentPane.add(btnEntrar);
		btnEntrar.addActionListener(new btnEntrarListener());
		
		lblUsername = new JLabel("UserName");
		lblUsername.setForeground(new Color(255, 255, 255));
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUsername.setBounds(320, 376, 80, 14);
		contentPane.add(lblUsername);
		
		lblPass = new JLabel("Pass");
		lblPass.setForeground(new Color(255, 255, 255));
		lblPass.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPass.setBounds(517, 376, 59, 14);
		contentPane.add(lblPass);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(VentanaPrincipal.class.getResource("/vuelos/aviato1.jpg")));
		label.setBounds(185, 78, 519, 191);
		contentPane.add(label);
		
		//oculto las cosas relacionada con identificacion en un inicio
		txtName.setVisible(false);
		passwordField.setVisible(false);
		lblUsername.setVisible(false);
		lblPass.setVisible(false);
		btnEntrar.setVisible(false);
	}
	
	private class adminlistener implements ActionListener{
	    public void actionPerformed(ActionEvent e){
	    	empleadoOadmin=false;
	    	txtName.setVisible(true);
	    	txtName.setText("admin");
	    	txtName.setEditable(false);
	    	lblUsername.setText("UserName");
			passwordField.setVisible(true);
			lblUsername.setVisible(true);
			lblPass.setVisible(true);
			btnEntrar.setVisible(true);
	    }
	}
	
	private class empleadolistener implements ActionListener{
	    public void actionPerformed(ActionEvent e){
	    	empleadoOadmin=true;
	    	lblUsername.setText("Legajo");
	    	txtName.setVisible(true);
	    	txtName.setEditable(true);
			passwordField.setVisible(true);
			lblUsername.setVisible(true);
			lblPass.setVisible(true);
			btnEntrar.setVisible(true);
			txtName.setText("");
	    }
	}
	
	private class btnEntrarListener implements ActionListener{
	    public void actionPerformed(ActionEvent e){
	    	if(empleadoOadmin){
	    		if(consultarEmpleado(txtName.getText(), passwordField.getText())){
	    			vistaEmpleados=new VentanaEmpleado(estaVentana,txtName.getText());
	    			vistaEmpleados.setVisible(true);
	    			cerrarVista();
	    		}
	    		else{
	    			JOptionPane.showMessageDialog(null, "Constraseña y/o usuario erroneo", "Error al ingresar", JOptionPane.ERROR_MESSAGE);
	    		}
	    	}
	    	else {
	    			if(consultarAdmin(passwordField.getText())){
	    				vistaAdmin=new VentanaAdmin(estaVentana,passwordField.getText());
	    				vistaAdmin.setVisible(true);
	    				cerrarVista();
	    			
	    			}//no se abre
	    			else{
	    				JOptionPane.showMessageDialog(null, "Constraseña Incorrecta", "Error al ingresar", JOptionPane.ERROR_MESSAGE);
	    			}
	    	}
	    	}
	}
	
	private void conectarBD()
	   {
	         try
	         {
	            String driver ="com.mysql.jdbc.Driver";
	        	String servidor = "localhost:3306";
	            String baseDatos = "vuelos";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
	   
	           c=DriverManager.getConnection(uriConexion, "empleado", "empleado");
	           
	           
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
	
	private boolean consultarEmpleado(String legajo, String pass){
		String consulta="select * from empleados where legajo='"+legajo+"' and password=md5('"+pass+"');";
		//para probar con otra bd sin md5
		//String consulta="select * from empleados where legajo='"+legajo+"' and password='"+pass+"';";
		boolean respuesta=true;
		try{
			Statement s=(Statement) c.createStatement();
			s.executeQuery(consulta);
			ResultSet rs=s.getResultSet();
			respuesta=rs.next();
			
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
		return respuesta;
	}
	private boolean consultarAdmin(String pass){
		 try
         {
        	String servidor = "localhost:3306";
            String baseDatos = "vuelos";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
   
            //trata de comunicarse con la BD vuelos con el usuario admin y el pass dado, si falla significa que no es el admin
           c=DriverManager.getConnection(uriConexion, "admin", pass);
         }
         catch (Exception ex) {return false;}
		 return true;
	}
	
	private void cerrarBD(){
		if (this.c != null)
	      {
	         try
	         {
	            c.close();
	            c = null;
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	      }
	}
	
	public void reActivar(){
		this.setVisible(true);
		conectarBD();
	}
	
	private void cerrarVista(){
		setVisible(false);
		txtName.setText("");
		passwordField.setText("");
		txtName.setVisible(false);
		passwordField.setVisible(false);
		btnEntrar.setVisible(false);
		lblPass.setVisible(false);
		lblUsername.setVisible(false);
		cerrarBD();
	}
	
	
}
