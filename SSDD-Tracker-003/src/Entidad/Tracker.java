package Entidad;

import java.util.Date;

public class Tracker {
	private int id;
	private boolean esMaster;
	private Date ultimoKA;
	
	public Tracker(int id, boolean esMaster, Date ultimoKA) {
		this.id = id;
		this.esMaster = esMaster;
		this.ultimoKA = ultimoKA;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean esMaster() {
		return esMaster;
	}

	public void setEsMaster(boolean esMaster) {
		this.esMaster = esMaster;
	}

	public Date getUltimoKA() {
		return ultimoKA;
	}

	public void setUltimoKA(Date ultimoKA) {
		this.ultimoKA = ultimoKA;
	}
}
