package cta;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Caret;
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
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;


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
		button_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JButton button_4 = new JButton("BORRAR");
		button_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JLabel lblConsultas = new JLabel("CONSULTAS");
		lblConsultas.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JComboBox comboBox = new JComboBox();
		
		JComboBox comboBox_1 = new JComboBox();
		
		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField.setColumns(10);
		
		filter = new JTextField();
		filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				catalogFilter=makeCatalogFilter(filter.getText());
			
			}
		});
		filter.setColumns(10);
		
		JButton button_5 = new JButton("GUARDAR");
		button_5.setFont(new Font("Dialog", Font.PLAIN, 12));
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
										.addComponent(comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(gl_panel_3.createSequentialGroup()
											.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
											.addGap(6)
											.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE))
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
						.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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

	 
}
