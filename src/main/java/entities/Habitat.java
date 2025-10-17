package entities;

import java.util.LinkedList;

public class Habitat {
	private int id;
	private String nombre;
	private LinkedList<String> caracteristicas = new LinkedList<>();
	private LinkedList<Bestia> bestias = new LinkedList<>();
	private String localizacion;
	
	public Habitat(int id, String nombre, LinkedList<String> caracteristicas, String localizacion) {
		setId(id);
		setNombre(nombre);
		setCaracteristicas(caracteristicas);
		setLocalizacion(localizacion);
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
	public String getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}
	public void setBestias(LinkedList<Bestia> bestias) {
		this.bestias = bestias;
	}
	public LinkedList<Bestia> getBestias() {
		return bestias;
	}

	@Override
	public String toString() {
		return (
				"ID: " + id
				+ "<br>Nombre: " + nombre
				+ "<br>Localización: " + localizacion
				+ "<br>Caracteristicas: " + caracteristicas
		);
	}
	
}
