package Modelo;

import java.util.List;
import java.util.Observer;

public class GestorDeRedundanciaDeTrackers {
	
	private List<Observer> observers;
	private int ID;
	private String IP;
	private int puerto;
		
	public GestorDeRedundanciaDeTrackers() {
		
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
	
	public static void main(String args[]) {
		
	}
}
