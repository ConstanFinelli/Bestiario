package entities;

import java.time.LocalDateTime;

public class Registro {
	private int nroRegistro;
	private String mainPic;
	private String introduccion;
	private String historia;
	private String descripcion;
	private String resumen;
	private LocalDateTime fechaAprobacion;
	private LocalDateTime fechaBaja;
	private Investigador publicador;
	private String estado;
	private Bestia bestia;
	
	public Registro(int id, String mainP, String i, String h, String d, String r, LocalDateTime fechaA, LocalDateTime fechaB, Investigador pub, String status, Bestia b) {
		nroRegistro = id;
		mainPic = mainP;
		introduccion = i;
		historia = i;
		descripcion = i;
		resumen = r;
		fechaAprobacion = fechaA;
		fechaBaja = fechaB;
		setPublicador(pub);
		estado = status;
		bestia = b;
	}
	
	public Registro(int id, Bestia b) {
		nroRegistro = id;
		bestia = b;
	}
	
	public int getNroRegistro() {
		return nroRegistro;
	}
	public void setNroRegistro(int nroRegistro) {
		this.nroRegistro = nroRegistro;
	}
	public String getIntroduccion() {
		return introduccion;
	}

	public void setIntroduccion(String introduccion) {
		this.introduccion = introduccion;
	}

	public String getHistoria() {
		return historia;
	}

	public void setHistoria(String historia) {
		this.historia = historia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public LocalDateTime getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(LocalDateTime fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public LocalDateTime getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(LocalDateTime fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public Investigador getPublicador() {
		return publicador;
	}

	public void setPublicador(Investigador publicador) {
		this.publicador = publicador;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Bestia getBestia() {
		return bestia;
	}

	public void setBestia(Bestia bestia) {
		this.bestia = bestia;
	}

	public String getMainPic() {
		return mainPic;
	}

	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}
	
	public void setContenido(String i, String h, String d, String r) {
		introduccion = i;
		historia = h;
		descripcion = d;
		resumen = r;
	}

	@Override public String toString() {
		return("Numero de Registro: " + nroRegistro + 
				"<br>Detalles: " + introduccion + historia + descripcion + resumen + 
				"<br>Fecha de aprobacion: " + fechaAprobacion +
				"<br>Fecha de baja: " + fechaBaja +
				"<br>Investigador que lo aprobo: " + publicador +
				"<br>Estado: " + estado);
	}

	
}
