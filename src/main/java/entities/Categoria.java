package entities;

public class Categoria{
	private int idCategoria;
	private String descripcionCategoria;
	
	public Categoria(int id, String desc) {
		this.setIdCategoria(id);
		this.setDescripcionCategoria(desc);
	}
	public int getIdCategoria() {
		return idCategoria;
	}
	
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
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
				+ "<br>Descripcion: " + descripcionCategoria
		);
	}
}

