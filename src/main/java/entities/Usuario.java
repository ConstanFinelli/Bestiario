package entities;

public class Usuario {
	private int idUsuario;
	private String correo;
	private String contraseña;
	private String estado;

	public Usuario(int id, String correo, String contraseña, String state) {
		this.idUsuario = id;
		this.correo = correo;
		this.contraseña = contraseña;
		this.estado = state;
	}

	public Usuario(String correo, String contraseña, String state) {
		this.correo = correo;
		this.contraseña = contraseña;
		this.estado = state;
	}
	
	public Usuario(int id, String inv) {
		idUsuario = id;
		estado = inv;
	}

	public Usuario(int id) {
		this.idUsuario = id;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	@Override
	public String toString() {
		return ("Id: " + idUsuario + "<br>Correo: " + correo + "<br>Contraseña:" + contraseña);
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String state) {
		this.estado = state;
	}
}
