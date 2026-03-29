package entities;

import java.util.LinkedList;

public class Habitat {
	private int id;
	private String nombre;
	private LinkedList<String> caracteristicas = new LinkedList<>();
	private LinkedList<Bestia> bestias = new LinkedList<>();
	private String localizacion;
	private double latitud;
	private double longitud;
	
	public Habitat(int id, String nombre, LinkedList<String> caracteristicas, String localizacion,  double latitud, double longitud) {
		setId(id);
		setNombre(nombre);
		setCaracteristicas(caracteristicas);
		setLocalizacion(localizacion);
		setLatitud(latitud);
		setLongitud(longitud);
	}
	
	public Habitat(int id, String nombre, String localizacion,  double latitud, double longitud) {
		setId(id);
		setNombre(nombre);
		setLocalizacion(localizacion);
		setLatitud(latitud);
		setLongitud(longitud);
	}
	
	public Habitat(int id) {
		setId(id);
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
	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public void setBestias(LinkedList<Bestia> bestias) {
		this.bestias = bestias;
	}
	public LinkedList<Bestia> getBestias() {
		return bestias;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localization) {
		localizacion = localization;
	}

	
}
