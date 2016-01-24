package Controlador;

import java.util.Observer;

import Modelo.GestorDeTrackerBitTorrent;

public class ControladorDetallesPeer {	
	
	public void iniciar(String IP, int puerto) {
		GestorDeTrackerBitTorrent.getInstance().iniciar(IP, puerto);
	}
	
	public void anadirObserver(Observer o) {
		GestorDeTrackerBitTorrent.getInstance().anadirObserver(o);
	}
}
