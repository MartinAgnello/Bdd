import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.mysql.jdbc.Statement;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;

public class VentanaInspector extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private VentanaPrincipal ventanaPrincipal;
	private String legajo;
	private String calle;
	private String altura;
	private String parquimetro;
	private JButton btnVolver;
	private java.sql.Connection conexion;
	private ArrayList<String> patentes;
	private GroupLayout panelPatente;
	private GroupLayout panelUbicacion;
	private JFormattedTextField patenteLetras;
	private JFormattedTextField patenteNumeros;
	private JLabel titulo;
	private JButton agregarPatente;
	private JButton conectarParquimetro;
	private JButton btnGenerarMultas;
	private JButton button;
	private JLabel mensaje;
	private JComboBox<String> cbCalle;
	private JLabel lblCalle;
	private JComboBox<String> cbAltura;
	private JLabel lblAltura;
	private JComboBox<String> cbParquimetro;
	private JLabel lblParquimetro;

	public VentanaInspector(VentanaPrincipal padre, String legajo) {
		setTitle("Ventana Inspector "+legajo);
		ventanaPrincipal = padre;
		this.legajo = legajo;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 519);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		setResizable(false);
		
		// Conexion a la base de datos y carga de datos en el JList listaTabla
		conectarBD();	

		agregarComponentes();
		
		
	}
		

	private void agregarComponentes() {
		crearPanelPatente();
		crearPanelUbicacion();
		visibilidadPanelPatentes(true);
		visibilidadPanelUbicacion(false);
		
		
		
		//Seteo listeners botones
		setListeners();		
	}
	
	
	private void crearPanelUbicacion(){
		
		cbCalle = new JComboBox<String>();	
		agregarOpcionesCalle();
		lblCalle = new JLabel("Calle");
		lblCalle.setFont(new Font("Tahoma", Font.BOLD, 15));		
		cbAltura = new JComboBox<String>();
		cbAltura.setEditable(false);		
		lblAltura = new JLabel("Altura");
		lblAltura.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnGenerarMultas = new JButton("Generar Multas");		
		cbParquimetro = new JComboBox<String>();	
		cbParquimetro.setEditable(false);
		lblParquimetro = new JLabel("Parquimetro");
		lblParquimetro.setFont(new Font("Tahoma", Font.BOLD, 15));		
		panelUbicacion = new GroupLayout(contentPane);
		panelUbicacion.setHorizontalGroup(
			panelUbicacion.createParallelGroup(Alignment.TRAILING)
				.addGroup(panelUbicacion.createSequentialGroup()
					.addContainerGap(214, Short.MAX_VALUE)
					.addGroup(panelUbicacion.createParallelGroup(Alignment.TRAILING)
						.addGroup(panelUbicacion.createSequentialGroup()
							.addComponent(lblParquimetro, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(cbParquimetro, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
						.addGroup(panelUbicacion.createParallelGroup(Alignment.TRAILING, false)
							.addGroup(panelUbicacion.createSequentialGroup()
								.addComponent(lblAltura, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGap(28)
								.addComponent(cbAltura, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
							.addGroup(panelUbicacion.createSequentialGroup()
								.addComponent(lblCalle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGap(63)
								.addComponent(cbCalle, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))))
					.addGap(225))
				.addGroup(Alignment.LEADING, panelUbicacion.createSequentialGroup()
					.addGap(301)
					.addComponent(btnGenerarMultas, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(295, Short.MAX_VALUE))
		);
		panelUbicacion.setVerticalGroup(
			panelUbicacion.createParallelGroup(Alignment.LEADING)
				.addGroup(panelUbicacion.createSequentialGroup()
					.addGap(136)
					.addGroup(panelUbicacion.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbCalle, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCalle))
					.addGap(44)
					.addGroup(panelUbicacion.createParallelGroup(Alignment.LEADING)
						.addComponent(cbAltura, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addGroup(panelUbicacion.createSequentialGroup()
							.addGap(6)
							.addComponent(lblAltura, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)))
					.addGap(42)
					.addGroup(panelUbicacion.createParallelGroup(Alignment.LEADING)
						.addComponent(cbParquimetro, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addGroup(panelUbicacion.createSequentialGroup()
							.addGap(6)
							.addComponent(lblParquimetro, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)))
					.addGap(54)
					.addComponent(btnGenerarMultas, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(58, Short.MAX_VALUE))
		);
		contentPane.setLayout(panelUbicacion);		
		
	}


	private void agregarOpcionesCalle(){
		String consulta="SELECT calle FROM parquimetros;";
		boolean respuesta;
		try{
			Statement s= (Statement) conexion.createStatement();
			s.executeQuery(consulta);
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				cbCalle.addItem(rs.getString(1).toString());
			}
			
			
			
		}catch (SQLException ex)     {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al Querer acceder.",
	                                       JOptionPane.ERROR_MESSAGE);	         
	      }
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	private void agregarOpcionesAltura(){
		System.out.println(calle);
		String consulta="SELECT altura FROM parquimetros WHERE calle='"+calle+"';";
		try{
			Statement s= (Statement) conexion.createStatement();
			s.executeQuery(consulta);
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				cbAltura.addItem(rs.getString(1).toString());
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
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	
	
	private void crearPanelPatente(){
		patentes = new ArrayList<String>();
		
		patenteLetras = new JFormattedTextField();
		patenteLetras.setToolTipText("AAA");		
		patenteNumeros = new JFormattedTextField();
		patenteNumeros.setToolTipText("111");
		
		titulo = new JLabel("Insertar Patente");
		titulo.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		agregarPatente = new JButton("Agregar Patente");
		
		conectarParquimetro = new JButton("Conectar a Parquimetro");
		
		btnVolver = new JButton("Volver");
		
		mensaje = new JLabel(" ");
		mensaje.setFont(new Font("Tahoma", Font.PLAIN, 11));
		panelPatente = new GroupLayout(contentPane);
		panelPatente.setHorizontalGroup(
			panelPatente.createParallelGroup(Alignment.LEADING)
				.addGroup(panelPatente.createSequentialGroup()
					.addGap(24)
					.addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(543, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, panelPatente.createSequentialGroup()
					.addGap(265)
					.addGroup(panelPatente.createParallelGroup(Alignment.TRAILING)
						.addComponent(agregarPatente, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
						.addComponent(mensaje, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, panelPatente.createParallelGroup(Alignment.LEADING, false)
							.addComponent(titulo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(panelPatente.createSequentialGroup()
								.addComponent(patenteLetras, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(patenteNumeros, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)))
						.addComponent(conectarParquimetro, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
					.addGap(302))
		);
		panelPatente.setVerticalGroup(
			panelPatente.createParallelGroup(Alignment.LEADING)
				.addGroup(panelPatente.createSequentialGroup()
					.addGap(126)
					.addComponent(titulo)
					.addGap(45)
					.addGroup(panelPatente.createParallelGroup(Alignment.BASELINE)
						.addComponent(patenteLetras, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addComponent(patenteNumeros, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
					.addGap(14)
					.addComponent(mensaje, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(agregarPatente, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addGap(17)
					.addComponent(conectarParquimetro, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
					.addComponent(btnVolver, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		contentPane.setLayout(panelPatente);
	}
	
		

	private void setListeners() {		

		agregarPatente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String patente = patenteLetras.getText()+""+patenteNumeros.getText();
				if(verificarPatente(patente)){
					patentes.add(patente);
					mensaje.setText("Patente "+patente+" agregada");					
				}else{
					mensaje.setText("La patente "+patente+" no está registrada");
					patenteLetras.setText("");
					patenteNumeros.setText("");
				}				
			}
		});

		conectarParquimetro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				visibilidadPanelPatentes(false);
				visibilidadPanelUbicacion(true);				
			}
		});

		cbCalle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				visibilidadPanelPatentes(false);
				calle = cbCalle.getItemAt(cbCalle.getSelectedIndex());
				cbAltura.removeAllItems();
				agregarOpcionesAltura();								
			}
		});
		

		btnGenerarMultas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		
		btnVolver.setFont(new Font("Calibri", Font.BOLD, 12));
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrarBD();
				dispose();
				ventanaPrincipal.restaurar();
			}
		});
		
	}
	
	private boolean verificarPatente(String patente){
		String consulta="select patente from automoviles where patente='"+patente+"';";
		boolean respuesta=true;
		try{
			Statement s=(Statement) conexion.createStatement();
			s.executeQuery(consulta);
			ResultSet rs= s.getResultSet();
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
	

	private void registrarMultas(ArrayList<String> patentes){
		
	}
	
	private void registrarAccesoParquimetro(String legajo, String parquimetro){
		
		String sentencia = "INSERT INTO accede (legajo,id_parq,fecha,hora) VALUES ('"+legajo+"','"+parquimetro+"',now(),now());";
		try{
			Statement s=(Statement) conexion.createStatement();
			s.execute(sentencia);			
			
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

	private boolean verificarUbicacionInspector(String legajo, String calle, String altura){
		String consulta="select * from asociado_con where calle='"+calle+"' and altura='"+altura+"' and legajo='"+legajo+"';";
		boolean respuesta=true;
		try{
			Statement s=(Statement) conexion.createStatement();
			s.executeQuery(consulta);
			ResultSet rs= s.getResultSet();
			respuesta=rs.next();
			
			//FALTA VERIFICA HORA
			
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
	

	private void conectarBD() {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String servidor = "localhost:3306";
			String baseDatos = "parquimetros";
			String url = "jdbc:mysql://" + servidor + "/" + baseDatos;
			conexion = DriverManager.getConnection(url, "inspector", "inspector");
	
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this,
					"Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	private void visibilidadPanelPatentes(boolean bool){
		patenteLetras.setVisible(bool);
		patenteNumeros.setVisible(bool);
		agregarPatente.setVisible(bool);
		conectarParquimetro.setVisible(bool);	
		titulo.setVisible(bool);
		mensaje.setVisible(bool);
		
		if(bool){
			contentPane.setLayout(panelPatente);
		}
		
	}
	private void visibilidadPanelUbicacion(boolean bool){
		lblCalle.setVisible(bool);
		lblAltura.setVisible(bool);
		lblParquimetro.setVisible(bool);
		cbCalle.setVisible(bool);
		cbAltura.setVisible(bool);
		cbParquimetro.setVisible(bool);
		btnGenerarMultas.setVisible(bool);
		
		if(bool){
			contentPane.setLayout(panelUbicacion);
		}
	}
	
	
	private void cerrarBD(){
		try{
		     if (this.conexion != null){
		        conexion.close();
		        conexion = null;
		     } 
		  }catch (SQLException ex){
		        System.out.println("SQLExcepcion " + ex.getMessage());
			    System.out.println("SQLEstado: " + ex.getSQLState());
			    System.out.println("CodigoError: " + ex.getErrorCode());
		 }	    
	}
}