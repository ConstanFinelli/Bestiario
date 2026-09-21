package logic;

import java.util.LinkedList;

import data.DataEvidencia;
import entities.Evidencia;
import entities.TipoEvidencia;
import exceptions.DataNotFoundException;
import helpers.CloudinaryHelper;

public class LogicEvidencia {
	private DataEvidencia eDAO = new DataEvidencia();
	
	public Evidencia getOne(Evidencia e) throws DataNotFoundException {
		return eDAO.getOne(e);
	}
	
	public LinkedList<Evidencia> findAll(){
		return eDAO.findAll();
	}
	
	public Evidencia save(Evidencia e) {
		return eDAO.save(e);
	}
	
	public Evidencia update(Evidencia e) throws DataNotFoundException {
		String fileId = getOne(e).getFileId();
		CloudinaryHelper.deleteImage(fileId);
		return eDAO.update(e);
	}
	
	public Evidencia delete(Evidencia e) throws DataNotFoundException {
		String fileId = getOne(e).getFileId();
		CloudinaryHelper.deleteImage(fileId);
		return eDAO.delete(e);
	}
	
	public LinkedList<Evidencia> findAllType(TipoEvidencia te){
		return eDAO.findAllType(te);
	}
	
	public boolean updateEstado(int nroEvidencia, int idTipoEvidencia, String nuevoEstado) throws DataNotFoundException {
		return eDAO.updateEstado(nroEvidencia, idTipoEvidencia, nuevoEstado);
	}
}
