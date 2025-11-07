package logic;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;

import data.DataUsuario;
import entities.Investigador;
import entities.Lector;
import entities.Usuario;



public class LogicUsuario {
	private DataUsuario usDAO = new DataUsuario();
	
	public Usuario getOne(Usuario us) {
		if(us.isEsInvestigador() == true) {
			us = (Investigador) usDAO.getOne(us);
		} else {
			us = (Lector) usDAO.getOne(us);
		}
		if(us != null) {
			us.setContraseña(dehashPassword(us.getContraseña()));
		}
		return us;
	}
	
	public LinkedList<Usuario> findAll() {
		LinkedList<Usuario> users = usDAO.findAll();
		if(!users.isEmpty()) {
			for(Usuario user : users) { // dehashea la contraseña por usuario
				user.setContraseña(dehashPassword(user.getContraseña()));
			}
		}
		return users;
	}
	
	public Usuario save(Usuario us) {
		if(us !=null) {
			us.setContraseña(hashPassword(us.getContraseña()));// hashea para guarda en bd
		}
		us = usDAO.save(us);
		us.setContraseña(dehashPassword(us.getContraseña())); // dehashea para mostrar a ui
		return us;
	}
	
	public Usuario update(Usuario us) {
		if(us != null) {
			us.setContraseña(hashPassword(us.getContraseña())); // hashea para guarda en bd
		}
		us = usDAO.update(us);
		us.setContraseña(dehashPassword(us.getContraseña())); // dehashea para mostrar a ui
		return us;
	}
	
	public static String hashPassword(String pass) {
		String encoded = Base64.getEncoder().encodeToString(pass.getBytes(StandardCharsets.UTF_8)); // hashea
		return encoded.toString();
    }
	
	public static String dehashPassword(String pass) {
		String decoded = new String(Base64.getDecoder().decode(pass.getBytes(StandardCharsets.UTF_8))); // desconvierte a contraseña original
		return decoded.toString();
	}
	
	public Usuario getByEmail(String correo) {
		Usuario us = usDAO.getByEmail(correo);
		return us;
	}
	
	public LinkedList<Investigador> findAllSolicitantes(){
		return usDAO.findAllSolicitantes();
	}
	
	public Usuario procesarSolicitud(Investigador us) {
		return usDAO.procesarSolicitud(us);
	}
}
