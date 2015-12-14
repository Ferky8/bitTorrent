package Controlador;

import java.util.Observer;

import Modelo.GestorDeRedundanciaDeTrackers;

public class ControladorDetallesTracker {
	
	public void anadirObserver(Observer o) {
		GestorDeRedundanciaDeTrackers.getInstance().anadirObserver(o);
	}
}
