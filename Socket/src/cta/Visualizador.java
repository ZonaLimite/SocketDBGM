package cta;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.text.DefaultCaret;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


public class Visualizador extends JFrame {

	/**
	 * 
	 */
	private Thread threadReceiver;
	private DatagramSocket socket;
	private Receiver runReceiver=null;	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private int flagDisconnect= 0;
	private String[] catalogFilter = {""};
	private JTextField comando;
	private JTextField textField;
	private JTextField filter;
	private JTextArea textArea;
	private JComboBox fenetre;
	private JSpinner spinner ;
	private JComboBox combo_Consultas;
	private JComboBox combo_Modulos;
	
	// Mapa contenedor de consultas registradas (memoria)
	private ConcurrentHashMap<String, Consulta> catalogoConsultas;
	
	//vector contenedor de modulos registrables (memoria)
	private Vector<Modulo> modulosRegistrables;
	
	
	public String[] getCatalogFilter() {
		return catalogFilter;
	}

	public JTextField getFilter() {
		return filter;
	}

	public void setFilter(JTextField filter) {
		this.filter = filter;
	}

	public int getFlagDisconnect() {
		return flagDisconnect;
	}

	public void setFlagDisconnect(int flagDisconnect) {
		this.flagDisconnect = flagDisconnect;
	}

	public JTextField getComando() {
		return comando;
	}

	public JTextArea getTextArea() {
		return textArea;
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Visualizador frame = new Visualizador();
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
	public Visualizador() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 952, 655);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpciones = new JMenuItem("Opciones");
		mnFile.add(mntmOpciones);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JButton btnClear = new JButton("CLEAR");
		btnClear.setHorizontalAlignment(SwingConstants.LEFT);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
			}
		});
		btnClear.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JScrollPane scrollPane = new JScrollPane();
		
		
		JPanel panel_2 = new JPanel();
		
		JPanel panel_3 = new JPanel();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 493, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(20, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(69, Short.MAX_VALUE))
		);
		
		JButton button_3 = new JButton("NEW");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newConsulta();
			}
		});
		button_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JButton button_4 = new JButton("BORRAR");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int confirmado = JOptionPane.showConfirmDialog(
						   contentPane,
						   "¿Borrar la consulta?");

						if (JOptionPane.OK_OPTION == confirmado)
							borrarConsultaActual(combo_Consultas.getSelectedItem().toString());
			}
		});
		button_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JLabel lblConsultas = new JLabel("FILTROS");
		lblConsultas.setFont(new Font("Dialog", Font.BOLD, 12));
		
		combo_Consultas = new JComboBox();
		combo_Consultas.addItemListener(new ItemListener() {
			//Selecionar la nueva consulta
			public void itemStateChanged(ItemEvent arg0) {
				if (!(arg0.getItem()== null))refreshObjectConsulta((String)arg0.getItem());
			}
		});
		
		combo_Modulos= new JComboBox();
		
		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField.setColumns(10);
		
		filter = new JTextField();
		filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		filter.setColumns(10);
		
		JButton button_5 = new JButton("GUARDAR");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				guardarConsultaActual(combo_Consultas.getSelectedItem().toString());
			}
		});
		button_5.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JButton btnNewButton = new JButton("Filtrar por modulo");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogAddModulo();
			}
		});
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup()
									.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING, false)
										.addComponent(combo_Consultas, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(gl_panel_3.createSequentialGroup()
											.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
											.addGap(6)
											.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)))
									.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel_3.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(combo_Modulos, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_panel_3.createSequentialGroup()
											.addGap(28)
											.addComponent(btnNewButton))))
								.addComponent(filter, GroupLayout.PREFERRED_SIZE, 343, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addComponent(button_5, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(41)
							.addComponent(lblConsultas, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(39, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap(24, Short.MAX_VALUE)
					.addComponent(lblConsultas)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
							.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnNewButton)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(combo_Modulos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(combo_Consultas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(button_5)
						.addComponent(filter, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
		);
		panel_3.setLayout(gl_panel_3);
		
		JLabel label = new JLabel("Maquina TOP");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 2, 1));
		
		JButton button = new JButton("Conectar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int top = (int)spinner.getValue();
				if(top==1 & fenetre.getSelectedItem().equals("Linea_Entrada")) {
					ConectarSocket("21.4.12.139");
				}
				if(top==2 & fenetre.getSelectedItem().equals("Linea_Entrada")) {
					ConectarSocket("21.4.12.149");
				}	
				if(top==1 & fenetre.getSelectedItem().equals("Carrusel")) {
					ConectarSocket("21.4.13.139");
				}
				if(top==2 & fenetre.getSelectedItem().equals("Carrusel")) {
					ConectarSocket("21.4.13.149");
				}
				if(top==1 & fenetre.getSelectedItem().equals("ATHS")) {
					ConectarSocket("21.4.14.139");
				}
				if(top==2 & fenetre.getSelectedItem().equals("ATHS")) {
					ConectarSocket("21.4.14.149");
				}								
				
			}
		});
		button.setFont(new Font("SansSerif", Font.PLAIN, 12));
		
		JButton button_1 = new JButton("Desconectar");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				disconnect();
			}
		});
		button_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		String[] fenetreStrings = { "Linea_Entrada", "Carrusel", "ATHS" };
		fenetre = new JComboBox(fenetreStrings);
		
		comando = new JTextField();
		comando.setFont(new Font("Dialog", Font.PLAIN, 14));
		comando.setColumns(10);
		
		JButton button_2 = new JButton("Enviar");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviarComando(comando.getText());
		
			}
		});
		button_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JLabel label_1 = new JLabel("SISTEMA :");
		label_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
							.addGap(3)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
							.addGap(31)
							.addComponent(button, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(81)
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(fenetre, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(2)
							.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(comando, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_2.createSequentialGroup()
					.addContainerGap(11, Short.MAX_VALUE)
					.addGap(11)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(1)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(1)
							.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)))
					.addGap(6)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(5)
							.addComponent(label_1))
						.addComponent(fenetre, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(button_2)
						.addComponent(comando, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
		);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 918, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(390)
							.addComponent(btnClear)))
					.addGap(12))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
					.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(0))
		);
		
		textArea = new JTextArea();
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		scrollPane.setViewportView(textArea);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
					.addGap(12))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(24)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
		
		//Inicializar estructuras de datos
		this.modulosRegistrables=this.initVectorModules();
		//this.initVectorConsultas();
		reloadCatalogoConsultas();
		
	}

	//Canal de envio de comandos
	public void enviarComando(String comando) {
		 byte[] mensaje_bytes = new byte[256];
		 String ip=null;
			int top = (int)spinner.getValue();
			if(top==1 & fenetre.getSelectedItem().equals("Linea_Entrada")) {
				ip="21.4.12.139";
			}
			if(top==2 & fenetre.getSelectedItem().equals("Linea_Entrada")) {
				ip="21.4.12.149";
			}						
		 
		 //Paquete
		 DatagramPacket paquete;
			 //PPC Linea de entrada 21.4.12.149
		 InetAddress address;
			try {
				address = InetAddress.getByName(ip);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			 mensaje_bytes = comando.getBytes();
			 paquete = new DatagramPacket(mensaje_bytes,comando.length(),address,5008);
			 try {
				if(!(socket==null))socket.send(paquete);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	
	 //Conectar Socket UDP a CDBGM
	 // Param    :IP servidor PPC
	 //Constants :port listen seer cdbgm -> 5008
	 public void ConectarSocket(String ipPPC) {
			//Definimos el sockets, número de bytes del buffer, y mensaje.
			 this.setFlagDisconnect(0);
			 InetAddress address;
			 byte[] mensaje_bytes = new byte[256];
			 String mensaje="";
			 mensaje_bytes=mensaje.getBytes();
			 //Paquete
			 DatagramPacket paquete;
			 try {
				 socket = new DatagramSocket();
				 //PPC Linea de entrada 21.4.12.149
				 address=InetAddress.getByName(ipPPC);
				 //mensaje = in.readLine();
				 mensaje = "h";
				 mensaje_bytes = mensaje.getBytes();
				 paquete = new DatagramPacket(mensaje_bytes,mensaje.length(),address,5008);
				 socket.send(paquete);
				 runReceiver = new Receiver(socket,this);
				 threadReceiver = new Thread(runReceiver);
				 threadReceiver.start();

				 
			 }catch(Exception e){
				 System.err.println("Error : "+ e.toString());
			 }
	 }
	 public void disconnect() {
		this.setFlagDisconnect(1);
		enviarComando("");
		socket=null;
	 }
	 
	 //Crea un array con un catalogo de cadenas como criterio para la inclusion de lineas
	 //Paramas: un String con una mascara de tipo "criterio1|criterio2|...criterion"
	 public String[] makeCatalogFilter(String maskFilter) {
		 String[] masks=null;
		 Vector<String> vMasker = new Vector<String>();
		 String mask = this.filter.getText();
	     StringTokenizer st = new StringTokenizer(mask,"|");
	     if(!st.hasMoreElements()) {

	    		vMasker.add(maskFilter); 

	     }else {
	    	 while(st.hasMoreElements()) {
				 vMasker.add(st.nextToken());
	    	 }
	     }
	     masks = new String[vMasker.size()];
	     for(int n=0; n < vMasker.size(); n++) {
	    	 masks[n]=vMasker.elementAt(n);
	    	
	     }
	     System.out.println(masks[0]);
		 return masks;
	 }
	 
	 //Inicializa el vector de modulos tratables
	 private Vector<Modulo> initVectorModules(){
		Vector<Modulo> vModules=new Vector<Modulo>();
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("cta/resources/CaptureBDGM3.csv");
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader fr = new BufferedReader(isr);
		String [] datosModulo = new String[3];
		//1: nombre modulo; 2: descripcion modulo; 3 : mask
		try {
			String strCurrentLine=fr.readLine();
			while(strCurrentLine != null){
				StringTokenizer st = new StringTokenizer(strCurrentLine,",");
				int n = 0;
				while(st.hasMoreElements()) {
					datosModulo[n]= st.nextToken();
					n++;
				}
				Modulo mod = new Modulo();
				mod.setNombre(datosModulo[0]);
				mod.setDescripcion(datosModulo[1]);
				mod.setMask(datosModulo[2]);
				vModules.add(mod);
				
				strCurrentLine=fr.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vModules;
	 }
	 
	 //Muestra cuadro eleccion modulo activable
	 private Modulo dialogAddModulo() {
		// Con JCombobox
		 Object selModulo = JOptionPane.showInputDialog(
		    contentPane,
		    "Seleccione modulo",
		    "Selector de modulos consulta activa",
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    modulosRegistrables.toArray(), 
		    "Seleccione Modulo");

		 	System.out.println("El usuario ha elegido "+selModulo);
		 	this.combo_Modulos.addItem(selModulo);
		 	return (Modulo)selModulo;
	 }
	 
	 //Inicializa el Vector de consultas registrable, leyendolo del fichero de registro serial
	 private void initVectorConsultas(){
		 //pendiente implementacion Leer registro consultas
		 catalogoConsultas = new ConcurrentHashMap<String,Consulta>();
	 }
	 
	 //Crea una nueva consulta. Pide un nombre y la registra
	 private void newConsulta() {
		// Con caja de texto
		 String nameConsulta = JOptionPane.showInputDialog(
		    contentPane,
		    "De un nombre a la nueva consulta",
		    null); 
		 if(nameConsulta==null)return;
		 // el icono sera un iterrogante
		    //borra el combo de modulos activos
		 	//crea una nueva entrada de consulta en el registro de consultas
		 	Consulta consulta = new Consulta();
		 	consulta.setNameConsulta(nameConsulta);
		 	consulta.setFiltro("");
		 	Vector<Modulo> vModulos = new Vector<Modulo>();
		 	consulta.setModulosActivos(vModulos);
		 	catalogoConsultas.put(nameConsulta, consulta);
		 	combo_Consultas.removeAllItems();
		 	filter.setText("");
		 	int index = 0;int indexComp=0;
		 	String item;
		 	for (Enumeration<String> enumConsultas = catalogoConsultas.keys(); enumConsultas.hasMoreElements();){
		 		combo_Consultas.addItem(item=(String)enumConsultas.nextElement());
		 		if(item.equals(nameConsulta))indexComp=index;
		 		index++;
		 	}
		 	combo_Consultas.setSelectedIndex(indexComp);
		 	
		 	System.out.println("El usuario ha añadido "+nameConsulta);
	 }
	 
	 //Actualiza estructuras de informacion consulta
	 private void refreshObjectConsulta(String nameConsulta) {
		 combo_Modulos.removeAllItems();
		 filter.setText(catalogoConsultas.get(nameConsulta).getFiltro());
		 Vector vModulos = catalogoConsultas.get(nameConsulta).getModulosActivos();
		 Iterator it = vModulos.iterator();
		 while(it.hasNext()) {
			 combo_Modulos.addItem(it.next());
		 }
		 //Pendiente Actualizar envio comando modulos activos
		 //Actualizar array masks
		 catalogFilter=makeCatalogFilter(filter.getText());
		 System.out.println("Refrescado objeto consulta");
	 }
	 
	 
	 
	 //Guarda la consulta actual al registro de consultas
	 //recoge los valores de texto en la caja filtro texto y 
	 //añade el vector de modulos presente el combo modulosActivos
	 private void guardarConsultaActual(String nameConsulta) {
		String filtroTextoActual = filter.getText();
		Vector<Modulo> vMod = new Vector();
		for (int  n = 0; n < combo_Modulos.getItemCount();n++) {
			vMod.add((Modulo)combo_Modulos.getItemAt(n));
		}

			Consulta newConsulta = new Consulta();
			newConsulta.setFiltro(filtroTextoActual);
			newConsulta.setNameConsulta(nameConsulta);
			newConsulta.setModulosActivos(vMod);
			if(catalogoConsultas.containsKey(nameConsulta)) {			
				catalogoConsultas.replace(nameConsulta, newConsulta);
			}else {
				catalogoConsultas.put(nameConsulta, newConsulta);
			}
			System.out.println("Consulta guardada en memoria");
			refreshObjectConsulta(nameConsulta);
			catalogFilter=makeCatalogFilter(filter.getText());
			//Aqui se puede serializar o no
			salvarCatalogoConsultas();
	 }
	 
	 //Borra la consulta actual al registro de consultas y refresca
	 //estructuras y filtros
	 private void borrarConsultaActual(String nameConsulta) {
		 combo_Consultas.removeItem(nameConsulta);
		 combo_Consultas.setSelectedIndex(-1);
		 combo_Modulos.removeAllItems();
		 filter.setText("");

		 catalogoConsultas.remove(nameConsulta);

		 
		 makeCatalogFilter("");
	 }
	 
	 //Serializa el objeto catalogoConsultas para conservar definiciones consultas
	 private void salvarCatalogoConsultas() {
			String ruta,fichero;
			URL url = Visualizador.class.getClassLoader().getResource(".");
			ruta = new File(url.getPath()).getAbsolutePath();
			fichero=ruta+"/catalogoConsultas.def";//
						
			  
			 try
		      {
		          ObjectOutputStream oos = new ObjectOutputStream(
		                  new FileOutputStream(new File(fichero)));
		          
		              oos.writeObject(this.catalogoConsultas);
		              oos.close();
		              System.out.println("Salvado catalogo");
		      } catch (Exception e)
		      {

		           e.printStackTrace();
		      }  
			  
		  
	 }
	 
	 private void reloadCatalogoConsultas() {
			String ruta,fichero;
			URL url = Visualizador.class.getClassLoader().getResource(".");
						ruta = new File(url.getPath()).getAbsolutePath();
						fichero=ruta+"/catalogoConsultas.def";//
						System.out.println(fichero);
	        try
	        	{
		            // Se crea un ObjectInputStream
		            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero));
		            
		            // Se lee el primer objeto
		            this.catalogoConsultas = (ConcurrentHashMap<String,Consulta>)ois.readObject();
		            ois.close();
		            
		            System.out.println("cargado catalogoConsultas ...");
		        }catch (Exception e1){
		           e1.printStackTrace();
		           this.initVectorConsultas();
		           
		        }
	        Enumeration enumKeys = catalogoConsultas.keys();
	        
	        for(Enumeration e =catalogoConsultas.keys();e.hasMoreElements(); ) {
	        	combo_Consultas.addItem(catalogoConsultas.get(e.nextElement()).toString());
	        	
	        }
	 }
}
