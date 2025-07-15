package entities;

public class CaracteristicaHabitat {
	private int idHabitat;
	private String descripcion;
	
	public CaracteristicaHabitat(int id, String desc) {
		setIdHabitat(id);
		setDescripcion(desc);
	}

	public int getIdHabitat() {
		return idHabitat;
	}

	public void setIdHabitat(int idHabitat) {
		this.idHabitat = idHabitat;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Override
	public String toString() {
		return getDescripcion();
	}
}
