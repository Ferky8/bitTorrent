package Modelo;

import java.util.List;
import java.util.Observer;

public class GestorDeRedundanciaDeTrackers {
	
	private List<Observer> observers;
		
	public GestorDeRedundanciaDeTrackers() {
		
	}

	public void añadirObserver(Observer o) {
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
	
	public static void main(String args[]) {
		
	}
}
