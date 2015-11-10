package Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

public class GestorDeRedundanciaDeTrackers extends Observable implements Runnable {
	
	private static GestorDeRedundanciaDeTrackers gestor = null;
	private List<Observer> observers;
	private int ID;
	private String IP;
	private int puerto;
	private MulticastSocket socket;
	private InetAddress group;
	private int estado = 0;
	private ConcurrentHashMap<Integer, GestorDeRedundanciaDeTrackers> trackers = new ConcurrentHashMap<Integer, GestorDeRedundanciaDeTrackers>();
	
	private Thread hilo;
		
	private GestorDeRedundanciaDeTrackers() {
		observers = new ArrayList<Observer>();
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
		try {
			socket.leaveGroup(group);
			this.alertarObservers(null);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	

	public void iniciar(String IP, int puerto, int ID) {
		this.IP = IP;
		this.puerto = puerto;
		this.ID = ID;
		this.observers = new ArrayList<Observer>();
		
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
	
	public void parar(int ID) {
		
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
	
	private void keepAlive() {
		
	}
	
	private void seleccionarMaster() {
		
	}
	
	private void nuevaInstancia() {
		String message = "400 Hola";
		
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
			
		} catch(Exception e) {
			
		}
	}
	
	public static void main(String args[]) {
		
	}
	
	public static GestorDeRedundanciaDeTrackers getInstance() {
		if(gestor == null)
			return new GestorDeRedundanciaDeTrackers();
		else
			return gestor;
	}

	@Override
	public void run() {
		switch(estado) {
			case 0: nuevaInstancia();
		}
	}
}
