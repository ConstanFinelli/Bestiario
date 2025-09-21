package entities;

public class ContenidoRegistro {
	private int idContenido;
	private String introduccion;
	private String historia;
	private String descripcion;
	private String resumen;
	
	public ContenidoRegistro(int idContenido, String introduccion, String historia, String descripcion,
			String resumen) {
		this.idContenido = idContenido;
		this.introduccion = introduccion;
		this.historia = historia;
		this.descripcion = descripcion;
		this.resumen = resumen;
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
	public int getIdContenido() {
		return idContenido;
	}
	public void setIdContenido(int idContenido) {
		this.idContenido = idContenido;
	}
}
