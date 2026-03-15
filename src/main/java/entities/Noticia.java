package entities;

import java.time.LocalDateTime;

public class Noticia {
	private int id;
	private String titulo;
	private String contenido;
	private String estado;
	private LocalDateTime fechaPublicacion;
	private Investigador publicador; //lo manejo como string porque sino no acepta nulos
	
	public Noticia(int id, String titulo, String contenido, String estado, LocalDateTime fechaPublicacion, Investigador publicador) {
		this.id = id;
		this.titulo = titulo;
		this.contenido = contenido;
		this.estado = estado;
		this.fechaPublicacion = fechaPublicacion;
		this.publicador = publicador;
	}
	
	public Noticia(int id) {
		this.id = id;
	}
	
	public Noticia() {
		// TODO Auto-generated constructor stub
	}

	public Noticia(String titulo, String contenido, String estado, LocalDateTime fechaPublicacion, Investigador publicador) {
		this.titulo = titulo;
		this.contenido = contenido;
		this.estado = estado;
		this.fechaPublicacion = fechaPublicacion;
		this.publicador = publicador;			
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public Investigador getPublicador() {
		return publicador;
	}
	public void setPublicador(Investigador publicador) {
		this.publicador = publicador;
	}
	
}
