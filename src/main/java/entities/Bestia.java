package entities;

import java.util.LinkedList;

public class Bestia {
	private int idBestia;
	private String nombre;
	private String peligrosidad;
	private LinkedList<Habitat> habitats = new LinkedList<>();
	private LinkedList<Categoria> categorias  = new LinkedList<>();
	private LinkedList<Registro> registros  = new LinkedList<>();
	
	public Bestia(int id, String name, String danger) {
		idBestia = id;
		nombre = name;
		peligrosidad = danger;
	}
	
	public Bestia(String name, String danger) {
		nombre = name;
		peligrosidad = danger;
	}
	
	public Bestia(int id) {
		idBestia = id;
	}
	
	public int getIdBestia() {
		return idBestia;
	}
	public void setIdBestia(int idBestia) {
		this.idBestia = idBestia;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPeligrosidad() {
		return peligrosidad;
	}
	public void setPeligrosidad(String peligrosidad) {
		this.peligrosidad = peligrosidad;
	}
	public LinkedList<Habitat> getHabitats(){
		return habitats;
	}
	public LinkedList<Categoria> getCategorias(){
		return categorias;
	}
	public LinkedList<Registro> getRegistros(){
		return registros;
	}
	public String listarHabitats() {
		String lista = "";
		for(Habitat hab: habitats) {
			lista = lista + "<br>" + hab.getNombre() + ",";
		}
		return lista;
	}

	public String listarCategorias() {
		String lista = "";
		for(Categoria cat: categorias) {
			lista = lista + "<br>" + cat.getDescripcionCategoria() + ",";
		}
		return lista;
	}
	public String listarRegistros() {
		String lista = "";
		for(Registro reg: registros) {
			lista = lista + "<br>" + reg.getNroRegistro() + reg.getDetalles() + reg.getFechaAprobacion().toString() +  reg.getEstado() + 
					reg.getFechaBaja().toString() + reg.getPublicador().getIdUsuario();
		}
		return lista;
	}
	
	 @Override public String toString(){
		 return("Id de Bestia: " + idBestia + 
				 "<br>Nombre: " + nombre + 
				 "<br>Peligrosidad: " + peligrosidad+
				 "<br>" + listarHabitats() + 
				 "<br>" + listarCategorias() + 
				 "<br>" + listarRegistros());
	 }
}
