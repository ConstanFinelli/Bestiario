package logic;


import java.time.LocalDateTime;
import java.util.LinkedList;

import data.DataRegistro;
import entities.Bestia;
import entities.Investigador;
import entities.Registro;
import exceptions.DataNotFoundException;
import helpers.CloudinaryHelper;
import helpers.EnvHelper;

public class LogicRegistro {
	public DataRegistro rDao = new DataRegistro();
	private LogicUsuario logicUsuario = new LogicUsuario();
	private LogicBestia logicBestia = new LogicBestia();
	
	public Registro getRegistroToShow(Bestia b, LocalDateTime fecha) {
		Registro r = rDao.getRegistroToShow(b, fecha);
		if (r != null) {
			r.setPublicador((Investigador) logicUsuario.getOne(r.getPublicador()));
		}
		return r;
	}
	
	public String getImagen(Bestia b, LocalDateTime fecha) {
		Registro registro = rDao.getRegistroToShow(b, fecha);
		return registro != null? registro.getMainPic() : EnvHelper.get("DEFAULT_PICTURE_ID");
	}
	
	public Registro getOne(Registro r) throws DataNotFoundException {
		Registro encontrado = rDao.getOne(r);
		encontrado.setPublicador((Investigador) logicUsuario.getOne(encontrado.getPublicador()));
		return encontrado;
	}
	
	public LinkedList<Registro> findAllByBestia(Bestia b){
		LinkedList<Registro> registros = rDao.findAllByBestia(b);
		for (Registro r : registros) {
			r.setPublicador((Investigador) logicUsuario.getOne(r.getPublicador()));
		}
		return registros;
	}
	
	public LinkedList<Registro> findRegistrosPendientes(Bestia b) {
		LinkedList<Registro> registros = rDao.findRegistrosPendientes(b);
		for (Registro r : registros) {
			r.setPublicador((Investigador) logicUsuario.getOne(r.getPublicador()));
		}
		return registros;
	}
	
	public LinkedList<Registro> findRegistrosAprobadosHoy() {
		LinkedList<Registro> registros = rDao.findRegistrosAprobadosHoy();
		for (Registro r : registros) {
			r.setPublicador((Investigador) logicUsuario.getOne(r.getPublicador()));
			r.setBestia(logicBestia.getOne(r.getBestia()));
		}
		return registros;
	}

	
	public Registro save(Registro r) {
		return rDao.save(r);
	}
	
	public Registro update(Registro r) throws DataNotFoundException {
		return rDao.update(r);
	}
	
	public Registro delete(Registro r) {
		return rDao.delete(r);
	}
	
	public void deleteImages(Bestia b) {
		LinkedList<Registro> registros = findAllByBestia(b);
		for(Registro registro: registros) {
			if (registro.getMainPic() != null && !registro.getMainPic().equals(EnvHelper.get("DEFAULT_PICTURE_ID"))) {
				CloudinaryHelper.deleteResource(registro.getMainPic(), "image");
			}
		}
	}
	
	public void updateEstado(Registro r) throws DataNotFoundException {
		rDao.updateEstado(r);
	}
	
}
