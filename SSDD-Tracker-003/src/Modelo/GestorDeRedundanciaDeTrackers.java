package Modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import Entidad.Tracker;

public class GestorDeRedundanciaDeTrackers extends Observable implements Runnable {
	
	private static GestorDeRedundanciaDeTrackers gestor = null;
	private List<Observer> observers;
	private static int ID;
	private String IP;
	private int puerto;
	private static boolean esMaster = false;
	private static MulticastSocket socket;
	private static InetAddress group;
	private int estado = 0;
	private int tamanioDB = 0;
	private boolean dbCompleta = false;
	private FileOutputStream fos = null;
	private String rutaDB;
	private static ConcurrentHashMap<Integer, Tracker> trackers;
	private static Timer timerKA;
	private static Timer timerCT;
	private GestorDeDatos gestorDeDatos;
	
	private Thread hilo;
		
	private GestorDeRedundanciaDeTrackers() {
		observers = new ArrayList<Observer>();
	}
	
	public static GestorDeRedundanciaDeTrackers getInstance() {
		if(gestor == null)
			gestor = new GestorDeRedundanciaDeTrackers();
		return gestor ;
	}

	public void anadirObserver(Observer o) {
		if (o != null && !observers.contains(o)) {
			observers.add(o);
		}
	}
	
	public void eliminarObserver(Observer o) {
		observers.remove(o);
	}
	
	private void alertarObservers(ConcurrentHashMap<Integer, Tracker> trackers) {
		for(Observer o : observers) {
			if (o != null) {
				o.update(this, trackers);
			}
		}
	}
	
	private void cambiarEstado() {
		switch(estado) {
			case 0: nuevaInstancia();
					break;
			case 1: keepAlive();
					comprobacionTrackers();	
					break;
		}
	}
	
	public void desconectar() {
		try {
			GestorDeRedundanciaDeTrackers.timerKA.cancel();
			GestorDeRedundanciaDeTrackers.timerCT.cancel();
			GestorDeRedundanciaDeTrackers.esMaster = false;
			if(GestorDeRedundanciaDeTrackers.ID != 1) {
				File f = new File("db/"+GestorDeRedundanciaDeTrackers.ID+"BaseDeDatos.db");
				if (f.exists()) f.delete();
			}
			GestorDeRedundanciaDeTrackers.trackers.remove(GestorDeRedundanciaDeTrackers.ID);
			GestorDeRedundanciaDeTrackers.socket.leaveGroup(GestorDeRedundanciaDeTrackers.group);
			this.alertarObservers(trackers);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	

	public void iniciar(String IP, int puerto, int ID) {
		this.IP = IP;
		this.puerto = puerto;
		GestorDeRedundanciaDeTrackers.ID = ID;
		
		try {
			GestorDeRedundanciaDeTrackers.socket = new MulticastSocket(puerto);
			GestorDeRedundanciaDeTrackers.group = InetAddress.getByName(IP);
			GestorDeRedundanciaDeTrackers.socket.joinGroup(group);
		} catch (SocketException e) {
			System.err.println("# Socket Error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# IO Error: " + e.getMessage());
		}
		trackers = new ConcurrentHashMap<Integer, Tracker>();
		
		hilo=new Thread(this,"hilo 1");
		hilo.start();
		cambiarEstado();
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
		timerKA = new Timer();
		timerKA.schedule( new TimerTask() {

            @Override
            public void run() {
            	
            	String mensaje = "";
            	if(esMaster) {
            		mensaje = "201KA-*"+Integer.toString(GestorDeRedundanciaDeTrackers.ID)+"$";
            	} else {
            		mensaje = "201KA-"+Integer.toString(GestorDeRedundanciaDeTrackers.ID)+"$";
            	}
            	try {
            		enviar(mensaje);
            	} catch(Exception e) {
    				
    			}
            }
        }, 0, 1000);
	}
	
	private void comprobacionTrackers() {
		timerCT = new Timer();
		timerCT.schedule( new TimerTask() {

            @Override
            public void run() {
            	//System.out.println("IDs de los trackers de hashmap: ");
            	for (int key : trackers.keySet()) {
            		
            		Tracker tracker = trackers.get(key);
            		
            		Date ahora = new Date();
            		long resta = ahora.getTime() - tracker.getUltimoKA().getTime();
            		
            		Date fechaResta = new Date(resta);
            		Calendar calendar = Calendar.getInstance();
            		calendar.setTime(fechaResta);
            		int seconds = calendar.get(Calendar.SECOND);
            		//int miliseconds = calendar.get(Calendar.MILLISECOND);
            		
                    //System.out.println("ID: " + key + " UKA: " + seconds + ":" + miliseconds);
            		
            		if(seconds > 2) {
            			if(tracker.esMaster()) {
            				String mensaje = "400MC-"+tracker.getId()+"$";
            				try {
								enviar(mensaje);
							} catch (IOException e) {
								e.printStackTrace();
							}
            			}
            			if(GestorDeRedundanciaDeTrackers.ID != 1) {
            				File f = new File("db/"+GestorDeRedundanciaDeTrackers.ID+"BaseDeDatos.db");
            				if (f.exists()) f.delete();
            			}
            			trackers.remove(key);
            			alertarObservers(trackers);
            		}
                }
            }
        }, 0, 2000);
	}
	
	private void actualizarTrackers(String mensaje) {
		
		if(mensaje.contains("*")) {
			mensaje = mensaje.substring(1);
			
			if(!trackers.containsKey(Integer.parseInt(mensaje))) {
				trackers.put(Integer.parseInt(mensaje), new Tracker(Integer.parseInt(mensaje), true, new Date()));
			} else {
				Tracker t = trackers.get(Integer.parseInt(mensaje));
				t.setUltimoKA(new Date());
				t.setEsMaster(true);
				trackers.put(Integer.parseInt(mensaje), t);
			}
		} else {
			if(!trackers.containsKey(Integer.parseInt(mensaje))) {
				trackers.put(Integer.parseInt(mensaje), new Tracker(Integer.parseInt(mensaje), false, new Date()));
			} else {
				Tracker t = trackers.get(Integer.parseInt(mensaje));
				t.setUltimoKA(new Date());
				t.setEsMaster(false);
				
				if(mensaje.contains("301"))
					t.setPreparadoGuardar(true);
				
				trackers.put(Integer.parseInt(mensaje), t);
			}
			
		}
	}
	
	private int getMinID() {
		
		Boolean encontrado = false;
		int min = 1;
		do{
			if(!trackers.containsKey(min))
			{
				encontrado = true;
			}else
			{
				min++;
			}
		}while(!encontrado);
		
		return min;
	}
	
	private void seleccionarMaster(String idMasterCaido) {
		int id = Integer.parseInt(idMasterCaido);
		if(trackers.containsKey(id)) {
			trackers.remove(id);
		}
		
		Boolean encontrado = false;
		int min = 1;
		do {
			if(trackers.containsKey(min))
			{
				encontrado = true;
			} else {
				min++;
			}
		} while(!encontrado);
		
		if(min == GestorDeRedundanciaDeTrackers.ID) {
			GestorDeRedundanciaDeTrackers.esMaster = true;
		}
			
		this.alertarObservers(trackers);
	}
	
	private void nuevaInstancia() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			
		}
		
		if(trackers.isEmpty()) {
			GestorDeRedundanciaDeTrackers.ID = 1;
			GestorDeRedundanciaDeTrackers.esMaster = true;
			gestorDeDatos = new GestorDeDatos("db/1BaseDeDatos.db");
			dbCompleta = true;
			estado = 1;
			cambiarEstado();
		} else {
			GestorDeRedundanciaDeTrackers.ID = 0;
			String mensaje = "200NI";
			
			try {
				enviar(mensaje);
			} catch(Exception e) {
				
			}
		}			
	}
	
	private void comprobarTodosOK() {
		boolean todosOK = true;
		
		for (int key : trackers.keySet()) {
    		Tracker tracker = trackers.get(key);
    		
    		if(!tracker.estaPreparadoGuardar()) {
    			todosOK = false;
    			break;
    		}    			
		}
		
		if(todosOK) {
			for (int key : trackers.keySet()) {
	    		Tracker tracker = trackers.get(key);
	    		tracker.setPreparadoGuardar(false);
	    		trackers.put(key, tracker);
			}
			
			String mensaje = "302GI";
			try {
				enviar(mensaje);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void guardarInformacion() {
		
	}
	
	public void peticionPeerRecibida() {
		if(GestorDeRedundanciaDeTrackers.esMaster) {
			Tracker t = trackers.get(GestorDeRedundanciaDeTrackers.ID);
			t.setPreparadoGuardar(true);
			trackers.put(GestorDeRedundanciaDeTrackers.ID, t);
			
			String mensaje = "300PG";
			try {
				enviar(mensaje);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class EnvioDBandID implements Runnable {
		@Override
		public void run() {
			int minID = getMinID();
			FileInputStream fis = null;
			String nombreDB = "db/"+GestorDeRedundanciaDeTrackers.ID+"BaseDeDatos.db";
			File f = new File(nombreDB);
			try {
		      fis = new FileInputStream(nombreDB);
		      enviar("202AI-"+minID+"#"+f.length()+"T");
		      sendBytes(fis);
		      fis.close();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	System.out.println("No abre fichero");
		    }			
		}
		private void sendBytes(FileInputStream fis) throws Exception {
			byte[] buffer = new byte[1020];
			int bytes = 0;
			String mensaje = "800-";
			while((bytes=fis.read(buffer)) != -1)
			{
				//System.out.println(bytes);
				byte[] buffer2 = new byte[mensaje.getBytes().length+bytes];
				System.arraycopy(mensaje.getBytes(), 0, buffer2, 0, mensaje.getBytes().length);
			    System.arraycopy(buffer, 0, buffer2, mensaje.getBytes().length, bytes);
				
				DatagramPacket messageOut = new DatagramPacket(buffer2, buffer2.length, group, puerto);
				socket.send(messageOut);
				//System.out.println(messageOut);
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

	@Override
	public void run() {
		try {
			while(true){
				byte[] buffer = new byte[1024];			
				DatagramPacket messageIn = null;
				messageIn = new DatagramPacket(buffer, buffer.length);
				socket.receive(messageIn);
				String mensaje = new String(messageIn.getData());
				System.out.println(" - Received a message from '" + messageIn.getAddress().getHostAddress() + ":" + messageIn.getPort() + "' -> " + new String(messageIn.getData()) + " [" + messageIn.getLength() + " byte(s)]");			
				
				if(mensaje.contains("201KA")) {
					int posInicio = mensaje.indexOf('-');
					int posFin = mensaje.indexOf('$');
					mensaje = mensaje.substring(posInicio+1, posFin);
					actualizarTrackers(mensaje);
					
				} else if(mensaje.contains("200NI") && GestorDeRedundanciaDeTrackers.esMaster) {
					System.out.println("Recibida peticion de nueva instancia...");
					Thread hiloEnvioDB = new Thread(new EnvioDBandID(),"hilo envio DB");
					hiloEnvioDB.start();
					
				} else if(mensaje.contains("202AI") && GestorDeRedundanciaDeTrackers.ID == 0) {
					//System.out.println(mensaje);
					int posInicio = mensaje.indexOf('-');
					int posFin = mensaje.indexOf('#');
					String id = mensaje.substring(posInicio+1, posFin);
					GestorDeRedundanciaDeTrackers.ID = Integer.parseInt(id);
					rutaDB = "db/"+GestorDeRedundanciaDeTrackers.ID+"BaseDeDatos.db";
					if(GestorDeRedundanciaDeTrackers.ID == 1) {
						dbCompleta = true;
						gestorDeDatos = new GestorDeDatos(rutaDB);
						estado = 1;
						cambiarEstado();
					} else {
						posInicio = posFin;
						posFin = mensaje.indexOf('T');
						tamanioDB = Integer.parseInt(mensaje.substring(posInicio+1, posFin));
					
						try {
							fos = new FileOutputStream(rutaDB);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				// Preparaos para guardar
				} else if(mensaje.contains("300PG") && !GestorDeRedundanciaDeTrackers.esMaster) {
					String respuesta = "301OK-"+GestorDeRedundanciaDeTrackers.ID+"$";
					enviar(respuesta);
					
				} else if(mensaje.contains("301OK") && GestorDeRedundanciaDeTrackers.esMaster) {
					actualizarTrackers(mensaje);
					comprobarTodosOK();
					
				} else if(mensaje.contains("302GI") && !GestorDeRedundanciaDeTrackers.esMaster) {
					guardarInformacion();
					
				} else if(mensaje.contains("400MC")) {
					int posInicio = mensaje.indexOf('-');
					int posFin = mensaje.indexOf('$');
					mensaje = mensaje.substring(posInicio+1, posFin);
					seleccionarMaster(mensaje);
					
				} else if(mensaje.contains("800-") && !dbCompleta) {
					byte[] bytesRecibidos = new byte[1020];
					System.arraycopy(messageIn.getData(), 4, bytesRecibidos, 0, bytesRecibidos.length);						
					
					fos.write(bytesRecibidos, 0, bytesRecibidos.length);
					
					File db = new File(rutaDB);
					//System.out.println(db.length());
					if(db.length() >= tamanioDB) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						dbCompleta = true;
						System.out.println("Completada la BD :)");
						gestorDeDatos = new GestorDeDatos(rutaDB);
						estado = 1;
						cambiarEstado();
					}
				}
				alertarObservers(trackers);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
