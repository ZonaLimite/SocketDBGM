package cta;



import java.awt.EventQueue;
import java.net.*;

 
//declaramos la clase udp
 public class InputSocket {
 
 private Visualizador vis;

 
 public static void main(String[] args) {
	 InputSocket iSocket = new InputSocket();
		
 }
 
 public InputSocket () {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					vis = new Visualizador( );
					vis.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	    
 }

 

 
}