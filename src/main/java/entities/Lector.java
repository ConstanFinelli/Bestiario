package entities;

import java.time.LocalDateTime;

public class Lector extends Usuario {
	private LocalDateTime fechaNacimiento;

	public Lector(int id, String correo, String contrasena, LocalDateTime fecha) {
		super(id, correo, contrasena, "lector");
		this.fechaNacimiento = fecha;
	}
	
	public Lector(String correo, String contrasena, LocalDateTime fecha) {
		super(correo, contrasena, "lector");
		this.fechaNacimiento = fecha;
	}
	
	public Lector(String correo, String contrasena, LocalDateTime fecha, String state) {
		super(correo, contrasena, state);
		this.fechaNacimiento = fecha;
	}
	
	public Lector(int id, String correo, String contrasena, LocalDateTime fecha, String state) {
		super(id, correo, contrasena, state);
		this.fechaNacimiento = fecha;
	}
	
	public LocalDateTime getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDateTime fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	@Override public String toString() {
		return (super.toString() +
				"<br>Fecha de nacimiento: " + fechaNacimiento);
				};
}
