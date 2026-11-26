package logic;

import java.util.LinkedList;

import data.DataEvidencia;
import entities.Bestia;
import entities.Evidencia;
import entities.TipoEvidencia;
import exceptions.DataNotFoundException;
import helpers.CloudinaryHelper;

public class LogicEvidencia {
	private DataEvidencia eDAO = new DataEvidencia();
	private LogicTipoEvidencia logicTipoEvidencia = new LogicTipoEvidencia();
	
	public Evidencia getOne(Evidencia e) throws DataNotFoundException {
		Evidencia encontrada = eDAO.getOne(e);
		encontrada.setTipo(logicTipoEvidencia.getOne(encontrada.getTipo()));
		return encontrada;
	}
	
	public LinkedList<Evidencia> findAll(){
		LinkedList<Evidencia> evidencias = eDAO.findAll();
		for (Evidencia ev : evidencias) {
			ev.setTipo(logicTipoEvidencia.getOne(ev.getTipo()));
		}
		return evidencias;
	}
	
	public LinkedList<Evidencia> findAllByBestia(Bestia b){
		LinkedList<Evidencia> evidencias = eDAO.findAllByBestia(b);
		for (Evidencia ev : evidencias) {
			ev.setTipo(logicTipoEvidencia.getOne(ev.getTipo()));
		}
		return evidencias;
	}
	
	public Evidencia save(Evidencia e) {
		return eDAO.save(e);
	}
	
	private void deleteCloudinaryResource(Evidencia e) throws DataNotFoundException {
		Evidencia actual = getOne(e);
		String resourceType = actual.getTipo() != null ? actual.getTipo().getResourceType() : null;
		CloudinaryHelper.deleteResource(actual.getFileId(), resourceType);
	}

	public Evidencia update(Evidencia e) throws DataNotFoundException {
		deleteCloudinaryResource(e);
		return eDAO.update(e);
	}
	
	public Evidencia delete(Evidencia e) throws DataNotFoundException {
		deleteCloudinaryResource(e);
		return eDAO.delete(e);
	}
	
	public LinkedList<Evidencia> findAllType(TipoEvidencia te){
		return eDAO.findAllType(te);
	}
	
	public boolean updateEstado(int nroEvidencia, int idTipoEvidencia, String nuevoEstado) throws DataNotFoundException {
		return eDAO.updateEstado(nroEvidencia, idTipoEvidencia, nuevoEstado);
	}
}
