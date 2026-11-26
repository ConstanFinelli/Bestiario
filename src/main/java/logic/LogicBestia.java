package logic;

import java.util.LinkedList;

import data.DataBestia;
import entities.Bestia;
import entities.Categoria;
import entities.Habitat;
import exceptions.DataNotFoundException;

public class LogicBestia {
	public DataBestia bDao = new DataBestia();
	private LogicHabitat logicHabitat = new LogicHabitat();
	private LogicCategoria logicCategoria = new LogicCategoria();
	private LogicEvidencia logicEvidencia = new LogicEvidencia();

	private void completarBestia(Bestia bestia) {
		if (bestia == null) {
			return;
		}
		bestia.setHabitats(logicHabitat.findAllByBestia(bestia));
		bestia.setCategorias(logicCategoria.findAllByBestia(bestia));
		bestia.setEvidencias(logicEvidencia.findAllByBestia(bestia));
	}

	private LinkedList<Bestia> completarBestias(LinkedList<Bestia> bestias) {
		for (Bestia bestia : bestias) {
			completarBestia(bestia);
		}
		return bestias;
	}
	
	public Bestia getOne(Bestia b) throws DataNotFoundException {
		Bestia bestiaEncontrada = bDao.getOne(b);
		completarBestia(bestiaEncontrada);
		return bestiaEncontrada;
	}
	
	public LinkedList<Bestia> findAll() {
		return completarBestias(bDao.findAll());
	}
	
	public LinkedList<Bestia> findByCategoria(String categoria){
		return completarBestias(bDao.findByCategoria(categoria));
	}

	public LinkedList<Bestia> filterByCategoria(LinkedList<Bestia> bestias, String idCategoria) {
		bestias.removeIf(bestia -> !bestia.hasCategoria(idCategoria));
		return bestias;
	}
	
	public LinkedList<Bestia> filterByHabitat(LinkedList<Bestia> bestias, String idHabitat) {
		bestias.removeIf(bestia -> !bestia.hasHabitat(idHabitat));
		return bestias;
	}
	
	public LinkedList<Bestia> findAllBestiasFromHabitat(Habitat ht) {
		return completarBestias(bDao.findAllBestiasFromHabitat(ht));
	}
	
	public Bestia save(Bestia b) {
		return bDao.save(b);
	}
	
	public void saveEvidencias(Bestia b) {
		bDao.saveEvidencias(b);
	}
	
	public void saveHabitats(Bestia b) {
		bDao.saveHabitats(b);
	}
	
	public void saveCategorias(Bestia b) {
		bDao.saveCategorias(b);
	}
	
	public void removeRelation(Bestia b, Habitat ht) {
		bDao.removeRelation(b, ht);
	}
	
	public void removeRelation(Bestia b, Categoria cat) {
		bDao.removeRelation(b, cat);
	}
	
	public Bestia delete(Bestia b) {
		return bDao.delete(b);
	}
	
	public Bestia update(Bestia besActualizada) throws DataNotFoundException {
			Bestia besAnterior = bDao.getOne(besActualizada);
			if(besAnterior != null) {
				if(besActualizada.getNombre() == null || besActualizada.getNombre().isBlank()) {
					besActualizada.setNombre(besAnterior.getNombre());
				}
				if(besActualizada.getPeligrosidad() == null || besActualizada.getPeligrosidad().isBlank()) {
					besActualizada.setPeligrosidad(besAnterior.getPeligrosidad());
				}
				return bDao.update(besActualizada);
			}
			return besAnterior;
}
	
	public void approve(Bestia b) throws DataNotFoundException {
		bDao.approve(b);
	}
	
	}
