package entities;

import java.time.LocalDate;

public class Registro {
	private int nroRegistro;
	private String detalles;
	private LocalDate fechaAprobacion;
	private LocalDate fechaBaja;
	private Investigador publicador;
	private String estado;
	private Bestia bestia;
	
	public Registro(int id, String dtls, LocalDate fechaA, LocalDate fechaB, Investigador pub, Bestia b) {
		nroRegistro = id;
		detalles = dtls;
		fechaAprobacion = fechaA;
		fechaBaja = fechaB;
		setPublicador(pub);
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
	public String getDetalles() {
		return detalles;
	}
	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	public LocalDate getFechaAprobacion() {
		return fechaAprobacion;
	}
	public void setFechaAprobacion(LocalDate fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	public LocalDate getFechaBaja() {
		return fechaBaja;
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

	@Override public String toString() {
		return("Numero de Registro: " + nroRegistro + 
				"<br>Detalles: " + detalles +
				"<br>Fecha de aprobacion: " + fechaAprobacion +
				"<br>Fecha de baja: " + fechaBaja +
				"<br>Investigador que lo aprobo: " + publicador +
				"<br>Estado: " + estado);
	}
	
}
