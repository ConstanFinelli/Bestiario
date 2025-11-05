package entities;

import java.time.LocalDateTime;

public class Comentario {
	private Usuario publicador;
	private Bestia  bestia;
	private LocalDateTime fecha;
	private String contenido;
	
	public Comentario(Usuario usuario, Bestia bestia, LocalDateTime fecha, String contenido) {
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
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	
}
