package logic;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import org.mindrot.jbcrypt.BCrypt;

import data.DataUsuario;
import entities.Investigador;
import entities.Usuario;
import exceptions.DataNotFoundException;

public class LogicUsuario {
	private DataUsuario usDAO = new DataUsuario();
	
	public Usuario getOne(Usuario us) throws DataNotFoundException {
		return usDAO.getOne(us);
	}
	
	public LinkedList<Usuario> findAll() {
		return usDAO.findAll();
	}
	
	public Usuario save(Usuario us) {
		if (us != null) {
			us.setContrasena(hashPassword(us.getContrasena()));
		}
		return usDAO.save(us);
	}
	
	public Usuario update(Usuario us) throws DataNotFoundException {
		return usDAO.update(us);
	}
	
	public static String hashPassword(String plainPassword) {
		if (plainPassword == null || plainPassword.isEmpty()) {
			return plainPassword;
		}
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
	}

	public boolean checkPassword(Usuario usuario, String plainPassword) {
		if (usuario == null || plainPassword == null) {
			return false;
		}
		String storedHash = usuario.getContrasena();
		if (storedHash == null || storedHash.isEmpty()) {
			return false;
		}

		// 1. Verificación moderna con BCrypt
		if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")) {
			try {
				return BCrypt.checkpw(plainPassword, storedHash);
			} catch (Exception e) {
				return false;
			}
		}

		// 2. Verificación de respaldo para migración de contraseñas legadas (Base64)
		try {
			String decoded = new String(java.util.Base64.getDecoder().decode(storedHash), java.nio.charset.StandardCharsets.UTF_8);
			if (plainPassword.equals(decoded)) {
				// Auto-migración a BCrypt en segundo plano
				String newHash = hashPassword(plainPassword);
				usDAO.updatePassword(usuario.getIdUsuario(), newHash);
				usuario.setContrasena(newHash);
				return true;
			}
		} catch (Exception ignored) {
			// No era Base64 válido
		}

		return false;
	}
	
	public Usuario getByEmail(String correo) throws DataNotFoundException {
		Usuario us = usDAO.getByEmail(correo);
		return us;
	}
	
	public LinkedList<Investigador> findAllSolicitantes(){
		return usDAO.findAllSolicitantes();
	}
	
	public LinkedList<Investigador> getCorreosInvestigadoresYRecibNot(){
		return usDAO.getCorreosInvestigadoresYRecibNot();
	}
	
	public Usuario delete(Usuario us) throws DataNotFoundException {
		return usDAO.delete(us);
	}

	public void updatePassword(int idUsuario, String newPassword) {
		usDAO.updatePassword(idUsuario, hashPassword(newPassword));
	}
}
