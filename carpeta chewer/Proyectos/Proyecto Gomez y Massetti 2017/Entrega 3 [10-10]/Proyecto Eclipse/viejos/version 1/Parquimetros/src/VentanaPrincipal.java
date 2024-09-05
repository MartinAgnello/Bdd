import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import com.mysql.jdbc.Connection;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JButton btnAdmin;
	private java.sql.Connection conexion;
	private JLabel lblSistemaDeParquimetros;
	private VentanaAdmin ventanaAdmin;

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

	public VentanaPrincipal() {
		setTitle("Sistema de Parquimetros");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setResizable(false);

		agregarComponentes();
		setListeners();
		conectarBD();

	}

	private void agregarComponentes() {
		btnAdmin = new JButton("Admin");
		btnAdmin.setFont(new Font("Calibri", Font.BOLD, 14));
		btnAdmin.setFocusPainted(false);
		
		lblSistemaDeParquimetros = new JLabel("Sistema de Parquimetros");
		lblSistemaDeParquimetros.setFont(new Font("Calibri", Font.BOLD, 28));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(198)
							.addComponent(lblSistemaDeParquimetros))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(277)
							.addComponent(btnAdmin, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(244, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(28)
					.addComponent(lblSistemaDeParquimetros)
					.addGap(29)
					.addComponent(btnAdmin, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(334, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}

	private void setListeners() {
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPasswordField pf = new JPasswordField();
				int okCxl = JOptionPane.showConfirmDialog(null, pf, "Ingrese password", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (okCxl == JOptionPane.OK_OPTION) {
					String password = new String(pf.getPassword());
					consultaAdmin(password);
				}
				
			}
		});
	}
	
	private void conectarBD() {
		try
        {
		//Intento de conectar a la base de datos
		   String servidor = "localhost:3306";
           String baseDatos = "parquimetros";
           String url = "jdbc:mysql://" + servidor + "/" + baseDatos;
  
          conexion= DriverManager.getConnection(url, "inspector", "inspector");
          
          
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(this,
                                          "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            System.out.println("SQLExcepcion: " + ex.getMessage());
            System.out.println("SQLEstado: " + ex.getSQLState());
            System.out.println("CodigoError: " + ex.getErrorCode());
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
	}
	
	private void consultaAdmin (String pass) {
		 try
         {
        	String servidor = "localhost:3306";
            String baseDatos = "parquimetros";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
   
            //Intento de conexion a la base con las password ingresada para el admin
           conexion =DriverManager.getConnection(uriConexion, "admin", pass);
           //Cambio a la ventana para el admin
           ventanaAdmin= new VentanaAdmin(this,pass);
           ventanaAdmin.setVisible(true);
           cerrarVistaActual();
         }
         catch (Exception ex) {
        	 String msg= "Password incorrecta - Intente nuevamente";
        	 JOptionPane.showMessageDialog(new JFrame(), msg, "Error",JOptionPane.ERROR_MESSAGE);
         }
		
	}
	
	private void cerrarVistaActual() {
		cerrarBD();
		this.setVisible(false);
	}
	
	private void cerrarBD() {
		if (this.conexion != null)
	      {
	         try
	         {
	            conexion.close();
	            conexion = null;
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLExcepcion: " + ex.getMessage());
	            System.out.println("SQLEstado: " + ex.getSQLState());
	            System.out.println("CodigoError: " + ex.getErrorCode());
	         }
	      }
	}
	
	public void restaurar() {
		setVisible(true);
		conectarBD();
	}
}