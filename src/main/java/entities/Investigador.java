package entities;

public class Investigador extends Usuario{
	private String nombre;
	private String apellido;
	private String dni;
	
	public Investigador() {
		
	}
	
	public Investigador(int id, String correo, String contrasena, String nombre, String apellido, String dni) {
		super(id, correo, contrasena, "investigador");
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
	}
	
	public Investigador(int id, String correo, String contrasena, String nombre, String apellido, String dni, String state) {
		super(id, correo, contrasena, state);
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
	}
	
	public Investigador(int id, String correo, String contrasena, String nombre, String apellido, String dni, String state, Boolean recibirNotificaciones) {
		super(id, correo, contrasena, state, recibirNotificaciones);
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		
	}
	
	public Investigador(String correo, String contrasena, String nombre, String apellido, String dni) {
		super(correo, contrasena, "investigador");
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
	}
	
	public Investigador(int id) {
		super(id);
	}
	
	public Investigador(int id, String inv) {
		super(id, inv);
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
