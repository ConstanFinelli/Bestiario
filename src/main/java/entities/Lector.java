package entities;

import java.time.LocalDate;

public class Lector extends Usuario {
	private LocalDate fechaNacimiento;

	public Lector(int id, String correo, String contraseña, LocalDate fecha) {
		super(id, correo, contraseña);
		this.fechaNacimiento = fecha;
	}
	
	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	@Override public String toString() {
		return (super.toString() +
				"<br>Fecha de nacimiento: " + fechaNacimiento)
				};
}
