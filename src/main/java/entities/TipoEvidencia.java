package entities;

public class TipoEvidencia {
	private int id;
	private String descripcion;
	
	public TipoEvidencia(int idinput, String desc) {
		this.id = idinput;
		this.setDescripcion(desc);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int idTipoEvidencia) {
		this.id = idTipoEvidencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
