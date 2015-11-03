package Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Observer;

public class GestorDeRedundanciaDeTrackers implements Runnable {
	
	private List<Observer> observers;
	private int ID;
	private String IP;
	private int puerto;
	private MulticastSocket socket;
	private InetAddress group;
	
	private Thread hilo;
		
	public GestorDeRedundanciaDeTrackers(String IP, int puerto, int ID) {
		this.IP = IP;
		this.puerto = puerto;
		this.ID = ID;
		
		try {
			this.socket = new MulticastSocket(puerto);
			this.group = InetAddress.getByName(IP);
			socket.joinGroup(group);
		} catch (SocketException e) {
			System.err.println("# Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# IO Error: " + e.getMessage());
		}
		
		hilo=new Thread(this,"hilo 1");
		hilo.start();
	}

	public void anadirObserver(Observer o) {
		if (o != null && !this.observers.contains(o)) {
			this.observers.add(o);
		}
	}
	
	public void eliminarObserver(Observer o) {
		this.observers.remove(o);
	}
	
	private void alertarObservers(Object param) {
		for(Observer o : this.observers) {
			if (o != null) {
				o.update(null, param);
			}
		}
	}
	
	public void desconectar() {
		this.alertarObservers(null);
	}
	

	public void iniciar(String IP, int puerto, int ID) {
		this.ID = ID;
		this.IP = IP;
		this.puerto = puerto;
		
	}
	
	public void parar(int ID) {
		
	}
	
	public void desconectar(int ID) {
		
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	public void keepAlive() {
		
	}
	
	public void seleccionarMaster() {
		
	}
	
	public void nuevaInstancia() {
		
	}
	
	public static void main(String args[]) {
		
	}

	@Override
	public void run() {
		String message = "Hola nigga";
		
		try {
			DatagramPacket messageOut = new DatagramPacket(message.getBytes(), message.length(), group, puerto);
			socket.send(messageOut);
			
			System.out.println(" - Sent a message to '" + messageOut.getAddress().getHostAddress() + ":" + messageOut.getPort() + 
			                   "' -> " + new String(messageOut.getData()) + " [" + messageOut.getLength() + " byte(s)]");
			
			byte[] buffer = new byte[1024];			
			DatagramPacket messageIn = null;
			
			for (int i = 0; i < 3; i++) { // get messages from other 2 peers in the same group
				System.out.println(" - After " + (3-i) + " messages I'll stop!");
	
				messageIn = new DatagramPacket(buffer, buffer.length);
				socket.receive(messageIn);
	
				System.out.println(" - Received a message from '" + messageIn.getAddress().getHostAddress() + ":" + messageIn.getPort() + 
		                   		   "' -> " + new String(messageIn.getData()) + " [" + messageIn.getLength() + " byte(s)]");			
			}
			
			System.out.println(" - I have received 3 messages, so I stopped!");
			
			socket.leaveGroup(group);
		} catch(Exception e) {
			
		}
	}
}
