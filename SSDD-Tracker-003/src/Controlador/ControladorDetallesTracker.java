package Controlador;

import java.util.Observer;

import Modelo.GestorDeRedundanciaDeTrackers;

public class ControladorDetallesTracker {

	private GestorDeRedundanciaDeTrackers gestorDeRedudancia;
	
	public void anadirObserver(Observer o) {
		gestorDeRedudancia.anadirObserver(o);
	}
	
	public void eliminarObserver(Observer o) {
		gestorDeRedudancia.eliminarObserver(o);
	}
	
	public void desconectar() {
		gestorDeRedudancia.desconectar();
	}
}
