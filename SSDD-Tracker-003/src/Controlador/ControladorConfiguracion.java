package Controlador;

import Modelo.GestorDeTrackerBitTorrent;

public class ControladorConfiguracion {
	private GestorDeTrackerBitTorrent gestorDeTracker;
	
	public void iniciar(String IP, int puerto, int ID) {
		gestorDeTracker.iniciar(IP, puerto, ID);
	}
	
	public void parar(int ID) {
		gestorDeTracker.parar(ID);
	}
	
	public void desconectar(int ID) {
		gestorDeTracker.desconectar(ID);
	}
}
