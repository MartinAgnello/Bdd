package Vuelos;
/**
autor: Juan Ignacio Cangelosi
**/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class VentanaReservas extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private VentanaPrincipal padre;
	private String legajo,nroV1,nroV2,fechaV1,fechaV2,diaV1,diaV2,clase1,clase2;
	private JComboBox<String> tipoDocs;
	private JButton btnRegistrar;
	private JLabel lblTipoDoc, lblNroDoc;
	private boolean soloIda;
	private Connection c;
	private CallableStatement stmt;
	
	/**
	 * Create the frame.
	 */
	public VentanaReservas(VentanaPrincipal ve,String l, String nroVuelo, String nroVuelo2,String fechaVuelo1, String fechaVuelo2, String diaVuelo1, String diaVuelo2, String nombreClase1, String nombreClase2) {
		legajo=l;
		padre=ve;
		nroV1=nroVuelo;
		nroV2=nroVuelo2;
		fechaV1=fechaVuelo1;
		fechaV2=fechaVuelo2;
		diaV1=diaVuelo1;
		diaV2=diaVuelo2;
		clase1=nombreClase1;
		clase2=nombreClase2;
		soloIda=false;
		conectarBD();
		//System.out.println("legajo: "+legajo+"  nro vuelo 1: "+nroVuelo+" nroVuelo 2: "+nroVuelo2+" fecha 1: "+fechaVuelo1+" fecha vuelo2: "+fechaVuelo2+ " \n dia v1:"+diaVuelo1+" diav2: "+diaVuelo2);
		inicializarGUI();
	}
	public VentanaReservas(VentanaPrincipal ve,String l, String nroVuelo,String fechaVuelo1, String diaVuelo1, String nombreClase1) {
		legajo=l;
		padre=ve;
		nroV1=nroVuelo;
		fechaV1=fechaVuelo1;
		diaV1=diaVuelo1;
		clase1=nombreClase1;
		soloIda=true;
		conectarBD();
		inicializarGUI();
	}
	
	private void conectarBD()
	   {
	         try
	         {
	            String driver ="com.mysql.jdbc.Driver";
	        	String servidor = "localhost:3306";
	            String baseDatos = "vuelos";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
	   
	            //establece una conexión con la  B.D. "batallas"  usando directamante una tabla DBTable    
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
	private void inicializarGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 811, 436);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(100, 149, 237));
		contentPane.setForeground(new Color(65, 105, 225));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tipoDocs = new JComboBox<String>();
		cargarComboBox();
		
		tipoDocs.setBounds(201, 174, 57, 20);
		contentPane.add(tipoDocs);
		
		textField = new JTextField();
		textField.setBounds(283, 174, 139, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setBounds(454, 173, 89, 23);
		contentPane.add(btnRegistrar);
		btnRegistrar.addActionListener(new btnRegistrarListener());
		
		lblTipoDoc = new JLabel("Tipo Doc");
		lblTipoDoc.setBounds(199, 149, 46, 14);
		contentPane.add(lblTipoDoc);
		
		lblNroDoc = new JLabel("Nro Doc");
		lblNroDoc.setBounds(283, 149, 46, 14);
		contentPane.add(lblNroDoc);
		
		JButton btnVolver = new JButton("Volver");
		btnVolver.setBounds(597, 23, 89, 23);
		getContentPane().add(btnVolver);
		btnVolver.addActionListener(new btnVolverListener());
		
		
	}
	
	private void cargarComboBox(){
		tipoDocs.addItem("DNI");
		tipoDocs.addItem("LC");
		tipoDocs.addItem("LE");
	}
	
	 private class btnRegistrarListener implements ActionListener{
		    public void actionPerformed(ActionEvent e){
		    	boolean docvalido=validarDoc(textField.getText());
		    	if(docvalido){
		    		try{
		    			if(soloIda){
		    				stmt=c.prepareCall("{call reservar_vuelo_ida(?,?,?,?,?,?,?,?)}");
		    				stmt.setString(1,(String) tipoDocs.getSelectedItem());
		    				stmt.setString(2, textField.getText());
		    				stmt.setString(3, legajo);
		    				stmt.setString(4, nroV1);
		    				stmt.setDate(5, new java.sql.Date(obtenerFecha(fechaV1).getTime()));
		    				stmt.setString(6, diaV1);
		    				stmt.setString(7, clase1);
		    				stmt.registerOutParameter(8, Types.VARCHAR);
		    				stmt.executeQuery();
		    				String salida=stmt.getString(8);
		    				JOptionPane.showMessageDialog(null,
		    	                    salida,
		    	                   	"Reserva de vuelos",
		    	                   	JOptionPane.INFORMATION_MESSAGE);
		    			}
		    			else{
		    				stmt=c.prepareCall("{call reservar_vuelo_idaYVuelta(?,?,?,?,?,?,?,?,?,?,?,?)}");
		    				stmt.setString(1,(String) tipoDocs.getSelectedItem());
		    				stmt.setString(2, textField.getText());
		    				stmt.setString(3, legajo);
		    				stmt.setString(4, nroV1);
		    				stmt.setString(5, nroV2);
		    				stmt.setDate(6, new java.sql.Date(obtenerFecha(fechaV1).getTime()));
		    				stmt.setDate(7, new java.sql.Date(obtenerFecha(fechaV2).getTime()));
		    				stmt.setString(8, diaV1);
		    				stmt.setString(9, diaV2);
		    				stmt.setString(10, clase1);
		    				stmt.setString(11, clase2);
		    				
		    				stmt.registerOutParameter(12, Types.VARCHAR);
		    				stmt.executeQuery();
		    				String salida=stmt.getString(12);
		    				JOptionPane.showMessageDialog(null,
		    	                    salida,
		    	                   	"Reserva de vuelos",
		    	                   	JOptionPane.INFORMATION_MESSAGE);
		    			}
		    		}catch(SQLException ex){
		    			ex.printStackTrace();
		    		}
		    	}
		    }
		}
	 private Date obtenerFecha(String s){
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		 Date fecha1=null;

		try {
				//if not valid, it will throw ParseException
				fecha1 = sdf.parse(s);

		} catch (ParseException e) {
				JOptionPane.showMessageDialog(this,
                       "Error no esperado con la fecha de la consulta",
                       "Error",
                       JOptionPane.ERROR_MESSAGE);
			}
		return fecha1;
	}
	
	private boolean validarDoc(String s){
		boolean valido=true;
		try{
			Integer.parseInt(s);
		}
		catch(Exception e){
			valido=false;
			JOptionPane.showMessageDialog(this,
                    "El Nro de documento introducido no puede contener letras ni otra cosa que no sea puntos",
                   	"Error",
                   	JOptionPane.ERROR_MESSAGE);
		}
		return valido;
	}
	 
	 private void cerrarBD(){
         try
         {
            c.close();

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
    	cerrarBD();
    	VentanaEmpleado ve=new VentanaEmpleado(padre,legajo);
    	ve.setVisible(true);
    	dispose();
    	
    }
}
	 
}
