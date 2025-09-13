package entities;

public class Investigador extends Usuario{
	private String nombre;
	private String apellido;
	private String dni;
	
	public Investigador(int id, String correo, String contraseña, String nombre, String apellido, String dni) {
		super(id, correo, contraseña);
		super.setEsInvestigador(true);
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
	}
	
	public Investigador(String correo, String contraseña, String nombre, String apellido, String dni) {
		super(correo, contraseña);
		super.setEsInvestigador(true);
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	@Override public String toString() {
		return (super.toString()+
				"<br>Nombre: " + nombre+
				"<br>Apellido: " + apellido+
				"<br>Dni: " + dni);
	}
}
