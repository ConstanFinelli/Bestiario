package entities;

import java.time.LocalDate;

public class Comentario {
	private Usuario publicador;
	private Bestia  bestia;
	private LocalDate fecha;
	private String contenido;
	
	public Comentario(Usuario usuario, Bestia bestia, LocalDate fecha, String contenido) {
		setPublicador(usuario);
		setBestia(bestia);
		setFecha(fecha);
		setContenido(contenido);
	}
	public Usuario getPublicador() {
		return publicador;
	}
	public void setPublicador(Usuario publicador) {
		this.publicador = publicador;
	}
	public Bestia getBestia() {
		return bestia;
	}
	public void setBestia(Bestia bestia) {
		this.bestia = bestia;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	
}
