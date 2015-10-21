package Controlador;

import Modelo.GestorDeTrackerBitTorrent;

public class ControladorConfiguracion {
	private GestorDeTrackerBitTorrent gestorDeTracker;
	
	public void iniciar() {
		gestorDeTracker.iniciar();
	}
	
	public void parar() {
		gestorDeTracker.parar();
	}
	
	public void desconectar() {
		gestorDeTracker.desconectar();
	}
}
