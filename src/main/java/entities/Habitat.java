package entities;

import java.util.LinkedList;

public class Habitat {
	private int id;
	private String nombre;
	private LinkedList<String> caracteristicas = new LinkedList<>();
	private LinkedList<Bestia> bestias = new LinkedList<>();
	private String latitud;
	private String longitud;
	
	public Habitat(int id, String nombre, LinkedList<String> caracteristicas, String latitud, String longitud) {
		setId(id);
		setNombre(nombre);
		setCaracteristicas(caracteristicas);
		setLatitud(latitud);
		setLongitud(longitud);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public LinkedList<String>  getCaracteristicas() {
		return caracteristicas;
	}
	public void setCaracteristicas(LinkedList<String>  caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public void setBestias(LinkedList<Bestia> bestias) {
		this.bestias = bestias;
	}
	public LinkedList<Bestia> getBestias() {
		return bestias;
	}

	
}
