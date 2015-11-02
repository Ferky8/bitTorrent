package Controlador;

import Modelo.GestorDeRedundanciaDeTrackers;

public class ControladorConfiguracion {
	private GestorDeRedundanciaDeTrackers gestorDeRedudancia;
	
	public void iniciar(String IP, int puerto, int ID) {
		gestorDeRedudancia = new GestorDeRedundanciaDeTrackers(IP, puerto, ID);
		//gestorDeRedudancia.iniciar(IP, puerto, ID);
	}
	
	public void parar(int ID) {
		gestorDeRedudancia.parar(ID);
	}
	
	public void desconectar(int ID) {
		gestorDeRedudancia.desconectar(ID);
	}
}
