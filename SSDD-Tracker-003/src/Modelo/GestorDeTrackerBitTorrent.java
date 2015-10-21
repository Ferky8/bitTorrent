package Modelo;

public class GestorDeTrackerBitTorrent {
	private int ID;
	private String IP;
	private int puerto;
		
	public GestorDeTrackerBitTorrent() {
		
	}
	
	public void iniciar(String IP, int puerto, int ID) {
		
	}
	
	public void parar(int ID) {
		
	}
	
	public void desconectar(int ID) {
		
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
}
