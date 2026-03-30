package logic;


import java.time.LocalDateTime;
import java.util.LinkedList;

import data.DataRegistro;
import entities.Registro;
import helpers.EnvHelper;
import entities.Bestia;

public class LogicRegistro {
	public DataRegistro rDao = new DataRegistro();
	
	public Registro getRegistroToShow(Bestia b, LocalDateTime fecha) {
		return rDao.getRegistroToShow(b, fecha);
	}
	
	public String getImagen(Bestia b, LocalDateTime fecha) {
		Registro registro = rDao.getRegistroToShow(b, fecha);
		return registro != null? registro.getMainPic() : EnvHelper.get("DEFAULT_PICTURE_ID");
	}
	
	public Registro getOne(Registro r) {
		return rDao.getOne(r);
	}
	
	public LinkedList<Registro> findRegistrosPendientes(Bestia b) {
		return rDao.findRegistrosPendientes(b);
	}
	
	public Registro save(Registro r) {
		return rDao.save(r);
	}
	
	public Registro update(Registro r) {
		return rDao.update(r);
	}
	
	public Registro delete(Registro r) {
		return rDao.delete(r);
	}
	
	public void updateEstado(Registro r) {
		rDao.updateEstado(r);
	}
	
}
