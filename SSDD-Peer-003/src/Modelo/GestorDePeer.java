package Modelo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class GestorDePeer implements Runnable {

	public static GestorDePeer gestor = null;
	private String IP;
	private int puerto;
	
	private Thread hilo;
	
	public static GestorDePeer getInstance() {
		if(gestor == null)
			gestor = new GestorDePeer();
		return gestor ;
	}
	
	public void iniciar(String IP, int puerto) {
		this.IP = IP;
		this.puerto = puerto;
		
		hilo=new Thread(this,"hilo Peer");
		hilo.start();
	}

	@Override
	public void run() {
		
			try (DatagramSocket udpSocket = new DatagramSocket()) {
			
				String message = "Hola";
				
				InetAddress serverHost = InetAddress.getByName(IP);			
				byte[] byteMsg = message.getBytes();
				DatagramPacket request = new DatagramPacket(byteMsg, byteMsg.length, serverHost, puerto);
				udpSocket.send(request);
				System.out.println(" - Sent request to '" + IP + ":" + request.getPort() + 
						           "' -> " + new String(request.getData()) + " [" + request.getLength() + " byte(s)]");
			} catch (Exception ex) {
				System.err.println("Error: " + ex.getMessage());
			}
	}
	
}
