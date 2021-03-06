package Controlador;

import java.util.Observer;

import Modelo.GestorDeRedundanciaDeTrackers;

public class ControladorConfiguracion {
	
	public void iniciar(String IP, int puerto, int ID) {
		GestorDeRedundanciaDeTrackers.getInstance().iniciar(IP, puerto, ID);
	}
	
	public void parar(int ID) {
		GestorDeRedundanciaDeTrackers.getInstance().parar(ID);
	}
	
	public void eliminarObserver(Observer o) {
		GestorDeRedundanciaDeTrackers.getInstance().eliminarObserver(o);
	}
	
	public void desconectar() {
		GestorDeRedundanciaDeTrackers.getInstance().desconectar();
	}
}
