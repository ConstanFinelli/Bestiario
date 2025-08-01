package entities;

import java.time.LocalDate;

public class Registro {
	private int nroRegistro;
	private String detalles;
	private LocalDate fechaAprobacion;
	private LocalDate fechaBaja;
	private Investigador publicador;
	private String estado;
	
	public Registro(int id, String dtls, LocalDate fechaA, LocalDate fechaB, Investigador pub) {
		nroRegistro = id;
		detalles = dtls;
		fechaAprobacion = fechaA;
		fechaBaja = fechaB;
		setPublicador(pub);
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
	
}
