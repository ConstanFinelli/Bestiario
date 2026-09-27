package entities;

public class Habitat {
	private int id;
	private String nombre;
	private String localizacion;
	private double latitud;
	private double longitud;
	
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

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localization) {
		localizacion = localization;
	}
	
	@Override
	public boolean equals(Object obj) {
		Habitat ht = null;
		try {
			ht = (Habitat) obj;
		}catch(ClassCastException e) {
			return false;
		}
		return ht.getId() == this.getId();
	}
}
