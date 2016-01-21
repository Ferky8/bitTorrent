package Controlador;

import java.util.List;

import Modelo.GestorDeDatos;
import Modelo.GestorDeTrackerBitTorrent;

public class ControladorDetallesPeer {	
	
	public void iniciar(String IP, int puerto) {
		GestorDeTrackerBitTorrent.getInstance().iniciar(IP, puerto);
	}
	
}
