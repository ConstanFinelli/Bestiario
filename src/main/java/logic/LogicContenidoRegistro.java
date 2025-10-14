package logic;

import data.DataContenidoRegistro;
import entities.ContenidoRegistro;


public class LogicContenidoRegistro {
private DataContenidoRegistro crDAO = new DataContenidoRegistro();
	
	public ContenidoRegistro getOne(int cr) {
		return crDAO.getOne(cr);
	}
	
	public ContenidoRegistro save(ContenidoRegistro cr) {
		return crDAO.save(cr);
	}
	
	public ContenidoRegistro update(ContenidoRegistro cr) {
		return crDAO.update(cr);
	}
}
