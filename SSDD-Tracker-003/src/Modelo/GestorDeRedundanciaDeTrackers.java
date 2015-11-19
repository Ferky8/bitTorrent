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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class GestorDeRedundanciaDeTrackers extends Observable implements Runnable {
	
	private static GestorDeRedundanciaDeTrackers gestor = null;
	private List<Observer> observers;
	private static int ID;
	private static String IP;
	private static int puerto;
	private static boolean esMaster = false;
	private static double ultimoKA;
	private static MulticastSocket socket;
	private static InetAddress group;
	private int estado = 0;
	private static ConcurrentHashMap<Integer, Integer> trackers;
	
	private Thread hilo;
	private Thread hiloLector;
		
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
			GestorDeRedundanciaDeTrackers.socket.leaveGroup(group);
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
			this.socket.joinGroup(group);
		} catch (SocketException e) {
			System.err.println("# Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# IO Error: " + e.getMessage());
		}
		trackers = new ConcurrentHashMap<Integer, Integer>();
		
		hiloLector = new Thread(new Lector(),"hilo lector");
		hiloLector.start();
		hilo=new Thread(this,"hilo 1");
		hilo.start();		
	}
	
	public void parar(int ID) {
		
	}
	
	class Lector implements Runnable {
		byte[] buffer = new byte[1024];			
		DatagramPacket messageIn = null;
		
		@Override
		public void run() {
			try {
				messageIn = new DatagramPacket(buffer, buffer.length);
				socket.receive(messageIn);
				String mensaje = new String(messageIn.getData());
				System.out.println(" - Received a message from '" + messageIn.getAddress().getHostAddress() + ":" + messageIn.getPort() + 
                		   "' -> " + new String(messageIn.getData()) + " [" + messageIn.getLength() + " byte(s)]");			
				
				if(mensaje.contains("$")) {
					int posicion = mensaje.indexOf('$');
					mensaje = mensaje.substring(0, posicion);
					System.out.println(mensaje);
					trackers.put(Integer.parseInt(mensaje), Integer.parseInt(mensaje));
				} else if(mensaje.contains("Hola") && GestorDeRedundanciaDeTrackers.esMaster) {
					enviar("5#");
					
				//falta tener en cuenta en el if de abajo que el gestor no tiene ID
				} else if(mensaje.contains("#")) {
					int posicion = mensaje.indexOf('#');
					System.out.println(posicion);
					mensaje = mensaje.substring(0, posicion);
					System.out.println(mensaje);
					trackers.put(Integer.parseInt(mensaje), Integer.parseInt(mensaje));
					GestorDeRedundanciaDeTrackers.ID = Integer.parseInt(mensaje);
					estado = 1;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
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
		Timer timer = new Timer();
        timer.schedule( new TimerTask() {

            @Override
            public void run() {
            	String mensaje = Integer.toString(GestorDeRedundanciaDeTrackers.ID)+'$';
            	try {
            		enviar(mensaje);
            	} catch(Exception e) {
    				
    			}
            }
        }, 0, 100);
	}
	
	
	private void seleccionarMaster() {
		
	}
	
	private void nuevaInstancia() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			
		}
		
		if(trackers.isEmpty()) {
			GestorDeRedundanciaDeTrackers.ID = 1;
			GestorDeRedundanciaDeTrackers.esMaster = true;
			estado = 1;
		} else {
			GestorDeRedundanciaDeTrackers.ID = 0;
			String mensaje = "Hola";
			
			try {
				enviar(mensaje);
			} catch(Exception e) {
				
			}
		}			
	}
	
	private synchronized void enviar(String mensaje) throws IOException {
		DatagramPacket messageOut = new DatagramPacket(mensaje.getBytes(), mensaje.length(), group, puerto);
		socket.send(messageOut);
		//System.out.println(" - Sent a message to '" + messageOut.getAddress().getHostAddress() + ":" + messageOut.getPort() + 
        //        "' -> " + new String(messageOut.getData()) + " [" + messageOut.getLength() + " byte(s)]");
	}
	
	public static void main(String args[]) {
		
	}
	
	public static GestorDeRedundanciaDeTrackers getInstance() {
		if(gestor == null)
			return new GestorDeRedundanciaDeTrackers();
		else
			return gestor ;
	}

	@Override
	public void run() {
		switch(estado) {
			case 0: nuevaInstancia();
			case 1: keepAlive();
		}
	}
}
