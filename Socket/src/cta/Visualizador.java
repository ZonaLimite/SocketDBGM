package cta;
import java.awt.Dimension;
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
import java.net.URI;
import java.net.URISyntaxException;
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
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


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
	private JTextField textfield_Mask;
	private JTextField filter;
	private JComboBox comboSistemas;
	private JSpinner spinner ;
	private JComboBox combo_Consultas;
	private JComboBox<Modulo> combo_Modulos;
	private JTextArea textArea;
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
		setBounds(100, 100, 1090, 864);
		
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
		

		
		JPanel panel_2 = new JPanel();
		
		JPanel panel_3 = new JPanel();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
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
		
		JLabel lblConsultas = new JLabel("CONSULTAS");
		lblConsultas.setFont(new Font("Dialog", Font.BOLD, 12));
		
		combo_Consultas = new JComboBox();
		combo_Consultas.addItemListener(new ItemListener() {
			//Selecionar la nueva consulta
			public void itemStateChanged(ItemEvent arg0) {
				System.out.println("El estado es :"+arg0.getStateChange()+" y el valor es:"+arg0.getItem().toString());
				if (arg0.getStateChange()==ItemEvent.SELECTED) {
						System.out.println("Entro");
						refreshObjectConsulta((String)arg0.getItem());
				}
			}
		});
		
		combo_Modulos= new JComboBox();
		combo_Modulos.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Modulo mod = (Modulo)arg0.getItem();
				textfield_Mask.setText(mod.getMask());
			}
		});
		
		textfield_Mask = new JTextField();
		textfield_Mask.setFont(new Font("Dialog", Font.PLAIN, 12));
		textfield_Mask.setColumns(10);
		
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
		
		JButton btnNewButton = new JButton("NEW");
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogAddModulo();
			}
		});
		
		JLabel lblNewLabel = new JLabel("MODULOS ACTIVOS");
		
		JButton btnBorrar = new JButton("BORRAR");//mODULO ACTIVO
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				borrarModuloActual((String)combo_Consultas.getSelectedItem(),(Modulo)combo_Modulos.getSelectedItem());
			}
		});
		btnBorrar.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JButton boton_Set_Mask = new JButton("SET");
		boton_Set_Mask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMaskModulo(textfield_Mask.getText());
			}

		});
		
		JButton boton_HelpMask = new JButton("HELP");
		boton_HelpMask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modulo modHelping = (Modulo) combo_Modulos.getSelectedItem();
				if(modHelping!=null) {
					mostrarAyudaModulo(modHelping);
				}

			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("MASCARAS");
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
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel_3.createSequentialGroup()
											.addComponent(btnNewButton)
											.addPreferredGap(ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
											.addComponent(btnBorrar))
										.addComponent(combo_Modulos, 0, 278, Short.MAX_VALUE)))
								.addComponent(filter, 442, 442, 442))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
								.addComponent(boton_HelpMask, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
								.addComponent(button_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
								.addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup()
									.addComponent(textfield_Mask, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(boton_Set_Mask, GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE))))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(41)
							.addComponent(lblConsultas, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addGap(58)
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 185, Short.MAX_VALUE)
							.addComponent(lblNewLabel_1)
							.addGap(24)))
					.addGap(18))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap(13, Short.MAX_VALUE)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblConsultas)
						.addComponent(lblNewLabel)
						.addComponent(lblNewLabel_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
							.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnBorrar, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addComponent(boton_HelpMask)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(combo_Consultas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
							.addComponent(textfield_Mask, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addComponent(boton_Set_Mask))
						.addComponent(combo_Modulos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(button_5)
						.addComponent(filter, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_3.setLayout(gl_panel_3);
		
		JLabel label = new JLabel("Maquina TOP");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 2, 1));
		
		JButton button = new JButton("Conectar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect();
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
		comboSistemas = new JComboBox(fenetreStrings);
		comboSistemas.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (!(arg0.getItem()== null && arg0.getStateChange()==1))
					combo_Modulos.removeAllItems();
					
					filter.setText("");
					disconnect();
					refreshComboConsultas("");
					modulosRegistrables=initVectorModules(comboSistemas.getSelectedItem()+".csv");
					connect();
				}
		});
		
		
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
							.addComponent(comboSistemas, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
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
						.addComponent(comboSistemas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGap(390)
							.addComponent(btnClear)))
					.addGap(12))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(0))
		);
	
		textArea = new JTextArea();
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					System.out.println("Texto Seleccionado:"+textArea.getSelectedText());
				}
			}
		});

		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(textArea);
		
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 1040, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(12, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(24)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE))
		);
		

	
		//textArea.setMinimumSize(new Dimension(300, 300));

		panel_1.setLayout(gl_panel_1);

		contentPane.setLayout(gl_contentPane);
		
		//Inicializar estructuras de datos
		this.modulosRegistrables=this.initVectorModules(this.comboSistemas.getSelectedItem()+".csv");
		//this.initVectorConsultas();
		reloadCatalogoConsultas();
		
	}

	public void mostrarAyudaModulo(Modulo mod) {
		//Consultas debe estar seleccionado en Comandos
		enviarComando("sc "+ mod.getNombre()+ " he");
		
		
	}

	//Canal de envio de comandos
	public void enviarComando(String comando) {
		 byte[] mensaje_bytes = new byte[256];
		 String ip=null;
			int top = (int)spinner.getValue();
			if(top==1 & comboSistemas.getSelectedItem().equals("Linea_Entrada")) {
				ip="21.4.12.139";
			}
			if(top==2 & comboSistemas.getSelectedItem().equals("Linea_Entrada")) {
				ip="21.4.12.149";
			}						
			if(top==1 & comboSistemas.getSelectedItem().equals("Carrusel")) {
				ip="21.4.13.139";
			}
			if(top==2 & comboSistemas.getSelectedItem().equals("Carrusel")) {
				ip="21.4.13.149";
			}
			if(top==1 & comboSistemas.getSelectedItem().equals("ATHS")) {
				ip="21.4.14.139";
			}
			if(top==2 & comboSistemas.getSelectedItem().equals("ATHS")) {
				ip="21.4.14.149";
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
	 public void connect() {
			int top = (int)spinner.getValue();
			if(top==1 & comboSistemas.getSelectedItem().equals("Linea_Entrada")) {
				ConectarSocket("21.4.12.139");
			}
			if(top==2 & comboSistemas.getSelectedItem().equals("Linea_Entrada")) {
				ConectarSocket("21.4.12.149");
			}	
			if(top==1 & comboSistemas.getSelectedItem().equals("Carrusel")) {
				ConectarSocket("21.4.13.139");
			}
			if(top==2 & comboSistemas.getSelectedItem().equals("Carrusel")) {
				ConectarSocket("21.4.13.149");
			}enviarComando("St ALL 0");
			if(top==1 & comboSistemas.getSelectedItem().equals("ATHS")) {
				ConectarSocket("21.4.14.139");
			}
			if(top==2 & comboSistemas.getSelectedItem().equals("ATHS")) {
				ConectarSocket("21.4.14.149");
			}					
	 }
	 
	 public void disconnect() {
		this.setFlagDisconnect(1);
		enviarComando("ST ALL 0");
		System.out.println("enviado ST ALL 0");
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
	 private Vector<Modulo> initVectorModules(String sistema){
		Vector<Modulo> vModules=new Vector<Modulo>();

		System.out.println("Accediendo a inputStream : cta/resources/" + sistema );
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("cta/resources/"+sistema);
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
				System.out.println("Descripcion :"+ mod.getDescripcion());
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
	 private void dialogAddModulo(){
		// Con JCombobox
		 Modulo miModuloClonado,clon = null;
		 Object selModulo = JOptionPane.showInputDialog(
		    contentPane,
		    "Seleccione modulo",
		    "Selector de modulos consulta activa",
		    JOptionPane.QUESTION_MESSAGE,
		    null,
		    modulosRegistrables.toArray(), 
		    "Seleccione Modulo");

		 	System.out.println("El usuario ha elegido "+selModulo);
		 	if(selModulo != null) {
		 		clon = (Modulo)selModulo;
		 		try {
					miModuloClonado = (Modulo) clon.clone();
			 		//enviarComando("ST "+mod.nombre+ " +ffffff");
			 		miModuloClonado.setMask("ffffff");
			 		textfield_Mask.setText("FFFFFF");
			 		System.out.println("Declarado modulo "+ miModuloClonado);
			 		this.combo_Modulos.addItem(miModuloClonado);
			 		this.combo_Modulos.setSelectedIndex(this.combo_Modulos.getItemCount()-1);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		 	}
		 	return;
		 	//return (Modulo)selModulo;
	 }

	private void setMaskModulo(String text) {
		Modulo modSelected = (Modulo) this.combo_Modulos.getSelectedItem();
		modSelected.setMask(text);
		int indexSelectedMod = this.combo_Modulos.getSelectedIndex();
		refreshComboModulos(this.combo_Consultas.getSelectedItem().toString());
		this.combo_Modulos.setSelectedIndex(indexSelectedMod);
			
	}
	 
	 //Inicializa el Vector de consultas 
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
		 	consulta.setSistemaConsulta((String) this.comboSistemas.getSelectedItem());
		 	consulta.setNameConsulta(nameConsulta);
		 	consulta.setFiltro("");
		 	Vector<Modulo> vModulos = new Vector<Modulo>();
		 	consulta.setModulosActivos(vModulos);
		 	//Indexamos la clave con el sistema al que pertenece
		 	String keySistemaConsulta = this.comboSistemas.getItemAt(this.comboSistemas.getSelectedIndex())+":"+nameConsulta;
		 	catalogoConsultas.put(keySistemaConsulta,consulta);
		 			 	enviarComando("ST ALL 0");
		 	filter.setText("");
	
		 	refreshComboConsultas(nameConsulta);
		 	
		 	System.out.println("El usuario ha añadido "+nameConsulta);
	 }
	 
	 private void refreshComboConsultas(String sConsulta) {
		 	int index = 0;int indexComp=0;
		 	combo_Consultas.removeAllItems();
		 	String item;
		 	for (Enumeration<String> enumConsultas = catalogoConsultas.keys(); enumConsultas.hasMoreElements();){
		 		StringTokenizer stEnumConsultas = new StringTokenizer(enumConsultas.nextElement(),":");
		 		String keyCurrentSistema = stEnumConsultas.nextToken();
		 		//Solo mostramos consultas filtradas para el sistema dado
		 		if(keyCurrentSistema.equals(this.comboSistemas.getSelectedItem().toString())) {
			 		combo_Consultas.addItem(item=stEnumConsultas.nextToken());
			 		if(item.equals(sConsulta))indexComp=index;
			 		index++;
		 		}
		 	}
		 	if(combo_Consultas.getItemCount()>0)combo_Consultas.setSelectedIndex(indexComp);
	}

	//Actualiza estructuras de informacion consulta
	 private void refreshObjectConsulta(String sConsulta) {
		 String nameConsulta = this.comboSistemas.getItemAt(this.comboSistemas.getSelectedIndex())+":"+sConsulta;
		 refreshComboModulos(sConsulta);
		 //Pendiente Actualizar envio comando modulos activos
		 //Actualizar array masks
		 filter.setText(catalogoConsultas.get(nameConsulta).getFiltro());		 
		 catalogFilter=makeCatalogFilter(filter.getText());
		 System.out.println("Refrescado objeto consulta");
	 }
	 
	 private void refreshComboModulos(String sConsulta) {
		 String nameConsulta = this.comboSistemas.getItemAt(this.comboSistemas.getSelectedIndex())+":"+sConsulta;
		 combo_Modulos.removeAllItems();
		 textfield_Mask.setText("");
		 enviarComando("ST ALL 0");
		 

		 Vector vModulos = catalogoConsultas.get(nameConsulta).getModulosActivos();
		 Iterator it = vModulos.iterator();
		
		 while(it.hasNext()) {
			 Modulo mod =(Modulo)it.next() ;
			
			 combo_Modulos.addItem(mod);
			 enviarComando("ST "+ mod.nombre+ " "+ mod.getMask()); 
			 System.out.println("Activado modulo "+ mod);
		 }
		
	}

	//Guarda la consulta actual al registro de consultas
	 //recoge los valores de texto en la caja filtro texto y 
	 //añade el vector de modulos presente el combo modulosActivos
	 private void guardarConsultaActual(String sConsulta) {
		String nameConsulta = this.comboSistemas.getItemAt(this.comboSistemas.getSelectedIndex())+":"+sConsulta;
		String filtroTextoActual = filter.getText();
		Vector<Modulo> vMod = new Vector();
		for (int  n = 0; n < combo_Modulos.getItemCount();n++) {
			Modulo modElegido = combo_Modulos.getItemAt(n);
			vMod.add(modElegido);
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
			refreshObjectConsulta(sConsulta);
			catalogFilter=makeCatalogFilter(filter.getText());
			//Aqui se puede serializar o no
			salvarCatalogoConsultas();
	 }
	 
	 //Borra la consulta actual al registro de consultas y refresca
	 //estructuras y filtros
	 private void borrarConsultaActual(String sConsulta) {
		 if(sConsulta==null)return;
		 String nameConsulta = this.comboSistemas.getItemAt(this.comboSistemas.getSelectedIndex())+":"+sConsulta;
		 Iterator iMods = (catalogoConsultas.get(nameConsulta)).modulosActivos.iterator();
		 while(iMods.hasNext()) {
			 Modulo mod = (Modulo)iMods.next();
			 borrarModuloActual(nameConsulta,mod);
		 }
		 combo_Consultas.removeItem(sConsulta);
		 combo_Consultas.setSelectedIndex(-1);
		
		 filter.setText("");

		 catalogoConsultas.remove(nameConsulta);
		 salvarCatalogoConsultas();
		 
		 makeCatalogFilter("");
		 System.out.println("El usuario ha eliminado la consulta : "+nameConsulta);
	 }
	 
	 //Borra el modulo actual seleccionado de la consulta seleccionada actual y lo
	 //desactiva
	 private void borrarModuloActual(String sConsulta,Modulo modSelected) {
		 String nameConsulta = this.comboSistemas.getItemAt(this.comboSistemas.getSelectedIndex())+":"+sConsulta;
		 Consulta consultaSelected = catalogoConsultas.get(nameConsulta);
		 if (consultaSelected==null)return;
		 
		 Vector<Modulo> modulosConsulta = consultaSelected.getModulosActivos();
		 if(modulosConsulta.contains(modSelected)) {
			 int index = modulosConsulta.indexOf(modSelected);
			 modulosConsulta.remove(index);
		 }
		 combo_Modulos.removeItem(modSelected);
		 textfield_Mask.setText("");
		 //enviar anulacion modulo activo a socket system
		 enviarComando("ST "+modSelected.nombre+" 0");
         System.out.println("desactivado modulo:"+modSelected);
		 
	 }
	 
	 //Serializa el objeto catalogoConsultas para conservar definiciones consultas
	 private void salvarCatalogoConsultas() {
			String ruta,fichero;
			URI url;
			try {
				url = Visualizador.class.getClassLoader().getResource(".").toURI();
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
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
						
			  
			 
			  
		  
	 }
	 
	 private void reloadCatalogoConsultas() {
			String ruta,fichero;
			URI url;
			try {
				url = Visualizador.class.getClassLoader().getResource(".").toURI();
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
		           //e1.printStackTrace();
		           this.initVectorConsultas();
		           
		        }
  
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
	        this.refreshComboConsultas("");//No nos situamos sobre ningun item en particular al init
	        	
	        
	 }
}
