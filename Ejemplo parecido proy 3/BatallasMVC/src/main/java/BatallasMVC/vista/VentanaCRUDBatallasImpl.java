package BatallasMVC.vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import com.toedter.calendar.JDateChooser;

import BatallasMVC.controlador.ControladorCRUDBatallas;
import BatallasMVC.modelo.BatallaBean;
import BatallasMVC.modelo.BatallaBeanImpl;
import Utils.*;


public class VentanaCRUDBatallasImpl extends JFrame implements VentanaCRUDBatallas {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(Conexion.class);
	
	private JPanel panelSuperior;
	private JLabel labelBatallas;
	private JScrollPane scrollPaneTabla;

	private JPanel panelInferior;
	
	private JPanel panelCampos;
	
	private DefaultTableModel batallasTableModel; 
	private JTable tablaBatallas;
	private JLabel labelNombreBatalla;
	private JTextField textFieldNombreBatalla;
	private JLabel labelFechaBatalla;
	private JDateChooser dateChooserFechaBatalla;
	
		
	private JPanel panelBotones;
	
	private JButton btnCrear;
	private JButton btnBuscar;
	private JButton btnActualizar;
	private JButton btnEliminar;
	private JButton btnLimpiar;
	
	// Bean para almacenar la batalla seleccionada en la tabla 
	private BatallaBean batallaSeleccionada= new BatallaBeanImpl();
	
	private ControladorCRUDBatallas controlador;
		
	public VentanaCRUDBatallasImpl() {
	      // inicializa todos los componentes de la ventana
		  inicializar();
	} 

	// Se setea controlador encargado de manejar los eventos de la vista (ventana)
	public void registrarControlador(ControladorCRUDBatallas controlador) {
		this.controlador = controlador;
	}
	
	// Método encargado de inicializar todos los componentes de la ventana
	private void inicializar() 
	{
		this.setTitle("Ejemplo CRUD (Create Read Update Delete) de batallas");		
		this.setSize(700, 400);		
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		//se crea un panel superior para contener la tabla de Batallas
		panelSuperior = new JPanel();
		getContentPane().add(panelSuperior, BorderLayout.NORTH);
									
		labelBatallas = new JLabel("Batallas ");
		panelSuperior.add(labelBatallas);
		
		//se crea un panel con scroll para contener la tabla de Batallas
		scrollPaneTabla = new JScrollPane();
		
		getContentPane().add(scrollPaneTabla, BorderLayout.CENTER);
		

		// se crea la tabla de Batallas y se agrega dentro del panel con scroll
		tablaBatallas = crearTablaBatallas();
		scrollPaneTabla.setViewportView(tablaBatallas);
		
		
		//se crea un panel inferior para contener las etiquetas, los campos de texto y los botones
		panelInferior = new JPanel();
		getContentPane().add(panelInferior, BorderLayout.SOUTH);
		panelInferior.setLayout(new BorderLayout());
		
		//se crea un panel para contener las etiquetas y los campos de texto
		panelCampos = new JPanel();
		panelInferior.add(panelCampos, BorderLayout.NORTH);
		panelCampos.setLayout(new GridLayout(2, 3, 2, 2));
		
		//se crean las etiquetas y los campos para el Nombre y la Fecha de una Batalla
		labelNombreBatalla = new JLabel("Nombre");
		panelCampos.add(labelNombreBatalla);
		labelNombreBatalla.setHorizontalAlignment(SwingConstants.TRAILING);
		
		textFieldNombreBatalla = new JTextField();
		panelCampos.add(textFieldNombreBatalla);
		textFieldNombreBatalla.setColumns(20);
		textFieldNombreBatalla.setHorizontalAlignment(SwingConstants.CENTER);
		
		labelFechaBatalla = new JLabel("Fecha");
		panelCampos.add(labelFechaBatalla);
		labelFechaBatalla.setHorizontalAlignment(SwingConstants.TRAILING);
		
        //se crea el campo para la fecha con una máscara para controlar el formato de fecha ingresado
		dateChooserFechaBatalla = new JDateChooser();		
		dateChooserFechaBatalla.setDateFormatString("dd/MM/yyyy");		
		panelCampos.add(dateChooserFechaBatalla);
		
		
						
		//se crean los botones para las operaciones CRUD sobre Batallas
		panelBotones = new JPanel();
		panelInferior.add(panelBotones, BorderLayout.SOUTH);
		panelBotones.setLayout(new GridLayout(0, 5, 4, 0));
		
		btnCrear = new JButton("Crear");		
		panelBotones.add(btnCrear);
		btnCrear.setToolTipText("Agregar una nueva batalla");
		
		btnBuscar = new JButton("Buscar");
		panelBotones.add(btnBuscar);
		btnBuscar.setToolTipText("Buscar batallas por nombre y/o fecha");
		
		btnActualizar = new JButton("Actualizar");
		panelBotones.add(btnActualizar);
		btnActualizar.setToolTipText("Actualizar la batalla seleccionada con un nuevo nombre y/o fecha");

		btnEliminar = new JButton("Eliminar");
		panelBotones.add(btnEliminar);
		btnEliminar.setToolTipText("Eliminar la batalla seleccionada");

		btnLimpiar = new JButton("Limpiar");
		panelBotones.add(btnLimpiar);
		btnEliminar.setToolTipText("Recargar la tabla y limpiar los campos nombre y fecha");
	
		// se registran los métodos del controlador encargados de manejar los eventos de la ventana (listeners) 
		registrarEventos();
	}
	
	
	// Método privado encargado de crear la tabla (Jtable) de Batallas para mostrar en la ventana  
	private JTable crearTablaBatallas() {	
	   //se crea un modelo que dará formato a la tabla de Batallas a partir de DefaulTableModel
       batallasTableModel = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
            //all cells are not editable
	          return false;
			}
	    };
			
	    
		//Se insertan los nombres las columnas en el modelo que se mostrarán en la tabla de Batallas
		batallasTableModel.addColumn("Nombre");		  
		batallasTableModel.addColumn("Fecha");
		
		//Se crea la tablaBatallas usando el modelo de tabla batallasTableModel
		tablaBatallas = new JTable(batallasTableModel);
		
		// activa el ordenamiento por columnas, para que se ordene al hacer click en una columna
		tablaBatallas.setAutoCreateRowSorter(true); 
                                                    	    
		
		tablaBatallas.setToolTipText("Click para seleccionar el registro.");
		//Se agrega un listener para poder selecionar un registro o fila de la tabla batallas 
		//al hacer click con el mouse        
        tablaBatallas.addMouseListener(new MouseAdapter() {
           public void mouseClicked(MouseEvent evt) {
              tablaMouseClicked(evt);          		
           }
        });  
		return tablaBatallas;
	} 
	
	// Método privado para asociar los métodos del controlador encargados de 
	// manejar los eventos de la ventana (listeners) 
	private void registrarEventos() {
		//Asocia el listener del botón Crear con el método crearBatalla(...) del controlador 
		 btnCrear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Ejecuta el listener del boton Crear");
				controlador.crearBatalla(textFieldNombreBatalla.getText(), dateChooserFechaBatalla.getDate());
			}
		});
		//Asocia el listener del botón Buscar con el método buscarBatallas(...) del controlador
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Ejecuta el listener del boton Buscar");
				controlador.buscarBatallas(textFieldNombreBatalla.getText(), dateChooserFechaBatalla.getDate());
			}
		});
		//Asocia el listener del botón Actualizar con el método actualizarBatallas(...) del controlador	
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Ejecuta el listener del boton Actualizar");
				controlador.actualizarBatalla(batallaSeleccionada, 
						      textFieldNombreBatalla.getText(), dateChooserFechaBatalla.getDate());
			}
		});
		//Asocia el listener del botón Eliminar con el método actualizarBatallas(...) del controlador
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Ejecuta el listener del boton Eliminar");
				controlador.eliminarBatalla(textFieldNombreBatalla.getText(), dateChooserFechaBatalla.getDate());
			}
		});
		
		//Asocia el listener del botón Limpiar con el método LimpiarBatallas(...) del controlador
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Ejecuta el listener del boton Limpiar");
				controlador.limpiarVista();
			}
		});		

	}
	
    // Método privado para cargar el campo de texto Nombre y el DateChooser Fecha con los valores 
	// correspondientes de la fila seleccionada al hacer click en la tabla Batallas y 
	// actualizar la propiedad batallaSeleccionada
     private void tablaMouseClicked(MouseEvent evt) 
 	  {   
 	      if ((this.tablaBatallas.getSelectedRow() != -1) && (evt.getClickCount() == 1))
 	      { 	
 	    	 String nombre=this.tablaBatallas.getValueAt(this.tablaBatallas.getSelectedRow(), 
 	    			 			                 		 batallasTableModel.findColumn("Nombre")).toString(); 
 	    	 String fecha= this.tablaBatallas.getValueAt(this.tablaBatallas.getSelectedRow(), 
                      									 batallasTableModel.findColumn("Fecha")).toString(); 	          
 
 	    	 try {
 	    		 batallaSeleccionada.setNombre(nombre); 	    	 
 	    		 batallaSeleccionada.setFecha(Fechas.convertirStringADate(fecha)); 	    	 
 	    	 
 	    		 this.textFieldNombreBatalla.setText(batallaSeleccionada.getNombre()); 	          	          
 	    		 this.dateChooserFechaBatalla.setDate(Fechas.convertirStringADate(fecha));
 	    	 }
 	    	 catch (ParseException ex) {
 	    		 logger.error("Excepcion de parseo al convertir el formato de la fecha: " + fecha);
 	    		 logger.error(ex.getMessage());	    	    	    		 
 	    	}
 	      }
 	  }    
        
        
    //Limpia el contenido de los campos para ingresar el Nombre y la Fecha de la Batalla
    public void limpiarCampos() {
    	dateChooserFechaBatalla.setDate(null);
     	textFieldNombreBatalla.setText("");
    }
     
	// método para mostrar una lista de batallas en la tabla (Jtable) a través de su modelo batallasTableModel
 	public void refrescarTablaBatallas(ArrayList<BatallaBean> lista) {
     
		logger.info("Muestra una lista de batallas en la tabla Batallas", lista.size());
		
		batallasTableModel.setRowCount(0);		
		
		for (BatallaBean b: lista) {
			String[] fila = new String[batallasTableModel.getColumnCount()];
			
			fila[batallasTableModel.findColumn("Nombre")] = b.getNombre();			
			fila[batallasTableModel.findColumn("Fecha")] = new SimpleDateFormat("dd/MM/YYYY").format(b.getFecha());
								
			batallasTableModel.addRow(fila);	 	   	
		}
		
	} 
			
	
 	//Muestra una ventana de modal con un mensaje y un título.
 	public void informar(String mensaje, String titulo) {
		logger.info("Crea una ventana modal informando sobre el mensaje");		
		JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.ERROR_MESSAGE);		
	}
	
	
	public void mostrarVentana() throws Exception {		
			this.setVisible(true);				
	}

	
	public void eliminarVentana() {
		logger.info("Eliminación de la ventana.");		
		this.dispose();
	}

	
	
	
}
