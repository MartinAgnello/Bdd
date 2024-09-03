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

public class VentanaInspector2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private VentanaPrincipal ventanaPrincipal;
	private String legajo;
	private String calle;
	private String altura;
	private String parquimetro;
	private JButton btnVolver;
	private JButton btnPatente;
	private JButton btnConfirmarUbicacion;
	private java.sql.Connection conexion;
	private JTextArea textoPatentes;
	private ArrayList<String> patentes;
	private JLabel titulo;
	private JLabel subtitulo;
	private JTextField[] ubicacion;
	private JLabel[] ubicacionTexto;
	private GroupLayout panelPatente;
	private GroupLayout panelUbicacion;

	public VentanaInspector2(VentanaPrincipal padre, String legajo) {
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

		agregarComponentes();
	}
		

	private void agregarComponentes() {
		
		
		
		
		
		
		this.crearPanelPatente();
		this.visibilidadPanelPatentes(true);		
		
		this.crearPanelUbicacion();
		this.visibilidadPanelUbicacion(false);
		
		//Seteo listeners botones
		setListeners();
		
		
		// Conexion a la base de datos y carga de datos en el JList listaTabla
		conectarBD();	
	}
	
	private void crearPanelUbicacion(){
		ubicacion = new JTextField[3];
		ubicacionTexto = new JLabel[3];
		
		ubicacion[0] = new JTextField();
		ubicacion[0].setColumns(10);		
		ubicacion[1] = new JTextField();
		ubicacion[1].setColumns(10);		
		ubicacion[2] = new JTextField();
		ubicacion[2].setColumns(10);
		
		ubicacionTexto[0] = new JLabel("CALLE");
		ubicacionTexto[0].setFont(new Font("Tahoma", Font.PLAIN, 16));
		ubicacionTexto[1] = new JLabel("ALTURA");
		ubicacionTexto[1].setFont(new Font("Tahoma", Font.PLAIN, 16));
		ubicacionTexto[2] = new JLabel("ID PARQUIMETRO");
		ubicacionTexto[2].setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		btnConfirmarUbicacion = new JButton("Confirmar");
		panelUbicacion = new GroupLayout(contentPane);
		panelUbicacion.setHorizontalGroup(
				panelUbicacion.createParallelGroup(Alignment.TRAILING)
				.addGroup(panelUbicacion.createSequentialGroup()
					.addGroup(panelUbicacion.createParallelGroup(Alignment.LEADING)
						.addGroup(panelUbicacion.createSequentialGroup()
							.addGap(179)
							.addGroup(panelUbicacion.createParallelGroup(Alignment.LEADING)
								.addComponent(ubicacionTexto[0])
								.addComponent(ubicacionTexto[1])
								.addComponent(ubicacionTexto[2]))
							.addGap(61)
							.addGroup(panelUbicacion.createParallelGroup(Alignment.LEADING)
								.addComponent(ubicacion[2], GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
								.addComponent(ubicacion[1], GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
								.addComponent(ubicacion[0], GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)))
						.addGroup(panelUbicacion.createSequentialGroup()
							.addGap(264)
							.addComponent(btnConfirmarUbicacion, GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(209, Short.MAX_VALUE))
		);
		panelUbicacion.setVerticalGroup(
				panelUbicacion.createParallelGroup(Alignment.LEADING)
				.addGroup(panelUbicacion.createSequentialGroup()
					.addGap(100)
					.addGroup(panelUbicacion.createParallelGroup(Alignment.BASELINE)
						.addComponent(ubicacionTexto[0])
						.addComponent(ubicacion[0], GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addGap(38)
					.addGroup(panelUbicacion.createParallelGroup(Alignment.BASELINE)
						.addComponent(ubicacion[1], GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(ubicacionTexto[1], GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addGroup(panelUbicacion.createParallelGroup(Alignment.BASELINE)
						.addComponent(ubicacion[2], GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(ubicacionTexto[2], GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
					.addComponent(btnConfirmarUbicacion, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addGap(77))
		);
	}
	
	
	private void crearPanelPatente(){
		// Label
			titulo = new JLabel("Ingrese las patentes");
			titulo.setFont(new Font("Calibri", Font.BOLD, 18));
			subtitulo = new JLabel("de los vehiculos estacionados");
			subtitulo.setFont(new Font("Calibri", Font.BOLD, 16));

			// Boton Volver
			btnVolver = new JButton("Volver");
			textoPatentes = new JTextArea();
			
			btnPatente = new JButton("Confirmar");		
			btnPatente.setFont(new Font("Calibri", Font.BOLD, 12));
			
			panelPatente = new GroupLayout(contentPane);
			panelPatente.setHorizontalGroup(
					panelPatente.createParallelGroup(Alignment.LEADING)
					.addGroup(panelPatente.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnVolver)
						.addGap(659))
					.addGroup(panelPatente.createSequentialGroup()
						.addGap(155)
						.addComponent(textoPatentes, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
						.addGap(181))
					.addGroup(panelPatente.createSequentialGroup()
						.addGap(241)
						.addComponent(btnPatente, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(269, Short.MAX_VALUE))
					.addGroup(panelPatente.createSequentialGroup()
						.addGap(262)
						.addComponent(titulo)
						.addContainerGap(323, Short.MAX_VALUE))
					.addGroup(panelPatente.createSequentialGroup()
						.addGap(233)
						.addComponent(subtitulo, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(287, Short.MAX_VALUE))
			);
			panelPatente.setVerticalGroup(
					panelPatente.createParallelGroup(Alignment.TRAILING)
					.addGroup(panelPatente.createSequentialGroup()
						.addGap(108)
						.addComponent(titulo)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(subtitulo, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(textoPatentes, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
						.addGap(32)
						.addComponent(btnPatente, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
						.addComponent(btnVolver)
						.addContainerGap())
			);

	}
	
	

	private void setListeners() {

		// boton confirmar batantes<
		btnConfirmarUbicacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				calle = ubicacion[0].getText();
				altura = ubicacion[1].getText();
				parquimetro = ubicacion[2].getText();				
				
				if(consultarUbicacionValida(calle,altura,parquimetro)){
					
					if(verificarUbicacionInspector(legajo,calle,altura)){
						registrarAccesoParquimetro(legajo,parquimetro);
						registrarMultas(patentes);						
						visibilidadPanelUbicacion(false);
						
						
					}else{
						JOptionPane.showMessageDialog(null, "No está asociado con el parquimetro", "Error", JOptionPane.WARNING_MESSAGE);			
						
					}
					
				}else{
					JOptionPane.showMessageDialog(null, "Opciones no válidas", "Error", JOptionPane.WARNING_MESSAGE);
					
				}								
			}
		});
		
		
		// boton confirmar batantes<
		btnPatente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				patentes = getPatentes(textoPatentes.getText());		
				visibilidadPanelPatentes(false);	
				visibilidadPanelUbicacion(true);
			}
		});
		
		
		// Listener boton volver
		btnVolver.setFont(new Font("Calibri", Font.BOLD, 12));
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrarBD();
				dispose();
				ventanaPrincipal.restaurar();
			}
		});
		
		
		
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
	
	
	
	private boolean consultarUbicacionValida(String calle, String altura, String parquimetro){
		String consulta="select * from parquimetros where calle='"+calle+"' and altura='"+altura+"' and id_parq='"+parquimetro+"';";
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
	
	
	private ArrayList<String> getPatentes(String patentes){
		ArrayList<String> lista = new ArrayList<String>();
		String patente = "";
		for(int i = 0; i<patentes.length(); i++){
			if(patentes.charAt(i)!=' '){
				patente += patentes.charAt(i);
				if((i+1)==patentes.length()){
					if(patente.length()==6)
						lista.add(patente);					
				}
			}else{
				if(patente.length()==6)
					lista.add(patente);
				patente = "";
			}
		}		
		
		return lista;
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
		textoPatentes.setVisible(bool);
		btnPatente.setVisible(bool);		
		titulo.setVisible(bool);
		subtitulo.setVisible(bool);		
		
		if(bool){
			contentPane.setLayout(panelPatente);
		}
		
	}
	private void visibilidadPanelUbicacion(boolean bool){
		ubicacion[0].setVisible(bool);
		ubicacion[1].setVisible(bool);
		ubicacion[2].setVisible(bool);
		ubicacionTexto[0].setVisible(bool);
		ubicacionTexto[1].setVisible(bool);
		ubicacionTexto[2].setVisible(bool);
		btnConfirmarUbicacion.setVisible(bool);	
		
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