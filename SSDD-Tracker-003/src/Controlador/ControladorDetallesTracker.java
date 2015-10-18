package Controlador;

import java.util.Observer;

import Modelo.GestorDeRedundanciaDeTrackers;

public class ControladorDetallesTracker {

	private GestorDeRedundanciaDeTrackers gestorDeRedudancia;
	
	public GestorDeRedundanciaDeTrackers getGestorDeRedudancia() {
		return gestorDeRedudancia;
	}

	public void setGestorDeRedudancia(
			GestorDeRedundanciaDeTrackers gestorDeRedudancia) {
		this.gestorDeRedudancia = gestorDeRedudancia;
	}

	public void añadirObserver(Observer o) {
		
	}
	
	public void eliminarObserver(Observer o) {
		
	}
	
	public void desconectar() {
		
	}
}
