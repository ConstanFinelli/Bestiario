package entities;

import java.time.LocalDateTime;

public class Lector extends Usuario {
	private LocalDateTime fechaNacimiento;

	public Lector(int id, String correo, String contraseña, LocalDateTime fecha) {
		super(id, correo, contraseña);
		this.fechaNacimiento = fecha;
	}
	
	public Lector(String correo, String contraseña, LocalDateTime fecha) {
		super(correo, contraseña);
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
