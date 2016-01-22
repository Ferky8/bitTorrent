package Controlador;

import Modelo.GestorDePeer;

public class ControladorPeer {	
	
	public void iniciar(String IP, int puerto) {
		GestorDePeer.getInstance().iniciar(IP, puerto);
	}
	
}
