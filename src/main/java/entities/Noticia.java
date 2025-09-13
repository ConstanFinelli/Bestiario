package entities;

import java.sql.Date;

public class Noticia {
	private int id;
	private String titulo;
	private String contenido;
	private String estado;
	private Date fechaPublicacion;
	private String idUsuario; //lo manejo como string porque sino no acepta nulos
	
	public Noticia(int id, String titulo, String contenido, String estado, Date fechaPublicacion, String idUsuario) {
		this.id = id;
		this.titulo = titulo;
		this.contenido = contenido;
		this.estado = estado;
		this.fechaPublicacion = fechaPublicacion;
		this.idUsuario = idUsuario;
	}
	
	public Noticia(int id) {
		this.id = id;
	}
	
	public Noticia() {
		// TODO Auto-generated constructor stub
	}

	public Noticia(String titulo, String contenido, String estado, Date fechaPublicacion, String idUsuario) {
		this.titulo = titulo;
		this.contenido = contenido;
		this.estado = estado;
		this.fechaPublicacion = fechaPublicacion;
		this.idUsuario = idUsuario;			
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
	public Date getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(Date fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public String getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	@Override
	public String toString() {
		return "Noticia [id=" + id + ", titulo=" + titulo + ", contenido=" + contenido + ", estado=" + estado
				+ ", fechaPublicacion=" + fechaPublicacion + ", idUsuario="+idUsuario +"]\n";
	}	
	
}
