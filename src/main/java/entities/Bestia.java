package entities;

import java.util.LinkedList;

public class Bestia {
	private int idBestia;
	private String nombre;
	private String peligrosidad;
	private LinkedList<Habitat> habitats = new LinkedList<>();
	private LinkedList<Categoria> categorias  = new LinkedList<>();
	private LinkedList<Registro> registros  = new LinkedList<>();
	private LinkedList<Comentario> comentarios = new LinkedList<>();
	private LinkedList<Evidencia> evidencias = new LinkedList<>();

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
	public void setHabitats(LinkedList<Habitat> habs){
		this.habitats = habs;
	}
	public LinkedList<Categoria> getCategorias(){
		return categorias;
	}
	public void setCategorias(LinkedList<Categoria> cats){
		this.categorias = cats;
	}
	public LinkedList<Registro> getRegistros(){
		return registros;
	}
	public void setRegistros(LinkedList<Registro> regs){
		this.registros = regs;
	}
	public LinkedList<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(LinkedList<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public String listarHabitats() {
		String lista = "";
		if(this.habitats != null) {
		for(Habitat hab: habitats) {
			if(hab != null) {
			lista = lista + "<br>" + hab.getNombre() + ",";
			}
		}}else {
			lista = "No hay habitats listadas";
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
			lista = lista + "<br>" + reg.getNroRegistro() + reg.getContenido().getResumen() + reg.getFechaAprobacion().toString() +  reg.getEstado() + 
					reg.getFechaBaja().toString() + reg.getPublicador().getIdUsuario();
		}
		return lista;
	}
	
	public String listarComentarios() {
		String lista = "";
		for(Comentario comentario: comentarios) {
			lista = lista + "<br>" + comentario.getBestia().getIdBestia() + comentario.getPublicador().getIdUsuario() + comentario.getFecha() +  comentario.getContenido();
		}
		return lista;
	}
	
	 @Override public String toString(){
		 return("Id de Bestia: " + idBestia + 
				 "<br>Nombre: " + nombre + 
				 "<br>Peligrosidad: " + peligrosidad+
				 "<br>" + listarHabitats() + 
				 "<br>" + listarCategorias() + 
				 "<br>" + listarRegistros() + 
				 "<br>" + listarComentarios());
	 }

	 public LinkedList<Evidencia> getEvidencias() {
		return evidencias;
	 }

	 public void setEvidencias(LinkedList<Evidencia> evidencias) {
		this.evidencias = evidencias;
	 }
}
