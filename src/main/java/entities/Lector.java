package entities;

import java.util.Date;

public class Lector extends Usuario {
	private Date fechaNacimiento;

	public Lector(int id, String correo, String contraseña, Date fecha) {
		super(id, correo, contraseña);
		this.fechaNacimiento = fecha;
	}
	
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	@Override public String toString() {
		return (super.toString() +
				"<br>Fecha de nacimiento: " + fechaNacimiento);
				};
}
