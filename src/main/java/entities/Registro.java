package entities;

import java.time.LocalDate;

public class Registro {
	private int nroRegistro;
	private String mainPic;
	private ContenidoRegistro contenido;
	private LocalDate fechaAprobacion;
	private LocalDate fechaBaja;
	private Investigador publicador;
	private String estado;
	private Bestia bestia;
	
	public Registro(int id, String mainP, ContenidoRegistro cR, LocalDate fechaA, LocalDate fechaB, Investigador pub, String status, Bestia b) {
		nroRegistro = id;
		mainPic = mainP;
		contenido = cR;
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
	public ContenidoRegistro getContenido() {
		return contenido;
	}
	public void setContenido(ContenidoRegistro contenido) {
		this.contenido = contenido;
	}
	public LocalDate getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(LocalDate fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public LocalDate getFechaBaja() {
		return fechaBaja != null ? fechaBaja : LocalDate.of(1970, 1, 1); // fecha genérica para evitar error en null
	}
	public void setFechaBaja(LocalDate fechaBaja) {
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

	@Override public String toString() {
		return("Numero de Registro: " + nroRegistro + 
				"<br>Detalles: " + contenido +
				"<br>Fecha de aprobacion: " + fechaAprobacion +
				"<br>Fecha de baja: " + fechaBaja +
				"<br>Investigador que lo aprobo: " + publicador +
				"<br>Estado: " + estado);
	}
	
}
