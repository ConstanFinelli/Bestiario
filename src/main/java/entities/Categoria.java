package entities;

public class Categoria{
	private int idCategoria;
	private String nombre;
	private String descripcionCategoria;
	
	public Categoria(int id, String name, String desc) {
		this.setIdCategoria(id);
		this.setNombre(name);
		this.setDescripcionCategoria(desc);
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDescripcionCategoria() {
		return descripcionCategoria;
	}
	
	public void setDescripcionCategoria(String descripcionCategoria) {
		this.descripcionCategoria = descripcionCategoria;
	}
	
	@Override public String toString() {
		return (
				"ID: " + idCategoria
				+ "<br>Nombre: " + nombre + 
				"<br>Descripcion: " + descripcionCategoria
		);
	}
}

