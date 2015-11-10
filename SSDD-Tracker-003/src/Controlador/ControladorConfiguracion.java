package Controlador;

import java.util.Observer;

import Modelo.GestorDeRedundanciaDeTrackers;
import Vista.Trackers;

public class ControladorConfiguracion {
	
	public void iniciar(String IP, int puerto, int ID) {
		GestorDeRedundanciaDeTrackers.getInstance().iniciar(IP, puerto, ID);
		//gestorDeRedudancia.iniciar(IP, puerto, ID);
	}
	
	public void parar(int ID) {
		GestorDeRedundanciaDeTrackers.getInstance().parar(ID);
	}
	
	public void desconectar() {
		GestorDeRedundanciaDeTrackers.getInstance().desconectar();
	}
	
	public void anadirObserver(Observer o) {
		GestorDeRedundanciaDeTrackers.getInstance().anadirObserver(o);
	}
}
