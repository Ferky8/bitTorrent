package Controlador;

import java.util.List;

import Modelo.GestorDeDatos;

public class ControladorDetallesPeer {
	private GestorDeDatos gestorDeDatos;
	
	public List<String> cargarDatos() {
		return gestorDeDatos.cargarDatos();
	}
}
