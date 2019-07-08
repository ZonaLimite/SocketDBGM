package cta;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;

public class Receiver implements Runnable {
	private DatagramSocket mySocket=null;
	private Visualizador vis;
	private String[] catalogFilter;
	private String sFilter;
	
    public void run() {
    	String cadenaMensaje;
    	
    	do {
    		byte[] RecogerServidor_bytes = new byte[1024];
    		
    		try {
    			//Esperamos a recibir un paquete
    			DatagramPacket servPaquete = new DatagramPacket(RecogerServidor_bytes,1024);
    			mySocket.receive(servPaquete);
    			String cadena1024 = new String(RecogerServidor_bytes).trim();

    			
    			//Convertimos el mensaje recibido en un string
    			//StringTokenizer st = new StringTokenizer(cadena1024,"#");
    			StringTokenizer st = new StringTokenizer(cadena1024,System.getProperty("line.separator"));
    			//Por cada Line
    			while(st.hasMoreTokens()) {
     					cadenaMensaje = st.nextToken();
     					String[] sArray = vis.getCatalogFilter();
     					//filtrar por catalogo de filtros
     					if((vis.getCatalogFilter())[0]=="") {
							vis.getTextArea().append(cadenaMensaje);
							vis.getTextArea().append(System.getProperty("line.separator"));
     					}else {
         					for(String sFilter:vis.getCatalogFilter()) {
             					//sFilter = vis.getFilter().getText();
         								//cONTROL FILTRO COPULATIVO
         								if(sFilter.contains("&")) {
         									String mask_AND[] = sFilter.split("&");
         									int match_AND=0;	
         									for(String s:mask_AND) {
         										if(cadenaMensaje.contains(s))match_AND++;
         									}
         									if(match_AND==mask_AND.length) {
         										handlerWriteLine(cadenaMensaje);
         									}
         								}else {
                 							if(cadenaMensaje.contains(sFilter)) {
                 								handlerWriteLine(cadenaMensaje);
                     						}

         								}
             				}
     						
     					}
    			}
    			
   		 	}catch (Exception e) {
   		 			System.err.println(e.getMessage());
   		 	}
    	} while (vis.getFlagDisconnect()==0);
    	mySocket = null;
    	vis.getTextArea().append("Hilo Finalizado");

    }
    public void handlerWriteLine(String cadena) {
    	    //Configuracion 1 
			//Solo Imprimimos el paquete recibido a caja visualizador
			vis.getTextArea().append(cadena);
			vis.getTextArea().append(System.getProperty("line.separator"));
			
			//Configuracion 2 
			// Imprimimos el paquete a visualizador y
			// Escribimos linea a fichero
			
			// Configuracion 3
			// Dispatching evento clase movingIngeneryInverse
    }

    public Receiver (DatagramSocket socket, Visualizador visualizador) {
    	mySocket = socket;
    	vis=visualizador;
    }


}
