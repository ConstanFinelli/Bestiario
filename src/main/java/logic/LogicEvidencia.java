package logic;

import java.util.LinkedList;

import entities.Evidencia;
import entities.TipoEvidencia;
import data.DataEvidencia;

public class LogicEvidencia {
	private DataEvidencia eDAO = new DataEvidencia();
	
	public Evidencia getOne(Evidencia e) {
		return eDAO.getOne(e);
	}
	
	public LinkedList<Evidencia> findAll(){
		return eDAO.findAll();
	}
	
	public Evidencia save(Evidencia e) {
		return eDAO.save(e);
	}
	
	public Evidencia update(Evidencia e) {
		return eDAO.update(e);
	}
	
	public Evidencia delete(Evidencia e) {
		return eDAO.delete(e);
	}
	
	public LinkedList<Evidencia> findAllType(TipoEvidencia te){
		return eDAO.findAllType(te);
	}
}
