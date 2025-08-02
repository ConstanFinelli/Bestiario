package entities;

import java.time.LocalDate;

public class Evidencia {
	private int nroEvidencia;
	private LocalDate fechaObtencion;
	private String estado;
	private String link;
	private TipoEvidencia tipo;
	
	public Evidencia(int nroEvidencia, LocalDate fechaObtencion, String estado, String link, TipoEvidencia tip) {
		this.nroEvidencia = nroEvidencia;
		this.fechaObtencion = fechaObtencion;
		this.estado = estado;
		this.link = link;
		this.tipo = tip;
	}
	public int getNroEvidencia() {
		return nroEvidencia;
	}
	public void setNroEvidencia(int nroEvidencia) {
		this.nroEvidencia = nroEvidencia;
	}
	public LocalDate getFechaObtencion() {
		return fechaObtencion;
	}
	public void setFechaObtencion(LocalDate fechaObtencion) {
		this.fechaObtencion = fechaObtencion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public TipoEvidencia getTipo() {
		return tipo;
	}
	public void setTipo(TipoEvidencia tipo) {
		this.tipo = tipo;
	}
	@Override public String toString() {
		return ("Numero de Evidencia: " + getNroEvidencia() + 
				"<br>Fecha de Obtencion: " + getFechaObtencion() +
				"<br>Estado: " + getEstado() +
				"<br>Enlace: " + getLink());
	}
	
}
