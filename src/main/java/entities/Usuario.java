package entities;

public class Usuario {
	private int idUsuario;
	private String correo; 
	private String contraseña;
	
	public Usuario(int id, String correo, String contraseña) {
		this.idUsuario = id;
		this.correo = correo;
		this.contraseña = contraseña;
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

	@Override public String toString() {
		return(
				"Id: " + idUsuario+
				"<br>Correo: " + correo+
				"<br>Contraseña:" + contraseña
				);
	} 
}
