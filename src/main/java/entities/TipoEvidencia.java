package entities;

public class TipoEvidencia {
	private int id;
	private String descripcion;
	private String resourceType;
	
	public TipoEvidencia(int idinput, String desc, String resourceType) {
		this.id = idinput;
		this.setDescripcion(desc);
		this.resourceType = resourceType;
	}
	
	public TipoEvidencia(String desc, String resourceType) {
		this.descripcion = desc;
		this.resourceType = resourceType;
	}
	
	public TipoEvidencia(int id) {
		this.id = id;
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

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
}
