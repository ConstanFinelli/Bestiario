package logic;

import java.util.LinkedList;

import data.DataBestia;
import entities.Bestia;
import entities.Habitat;

public class LogicBestia {
	public DataBestia bDao = new DataBestia();
	
	public Bestia getOne(Bestia b) {
		return bDao.getOne(b);
	}
	
	public LinkedList<Bestia> findAll() {
		return bDao.findAll();
	}
	
	public LinkedList<Bestia> findAllBestiasFromHabitat(Habitat ht) {
		return bDao.findAllBestiasFromHabitat(ht);
	}
	
	public Bestia save(Bestia b) {
		return bDao.save(b);
	}
	
	public Bestia delete(Bestia b) {
		return bDao.delete(b);
	}
	
		public Bestia update(Bestia besActualizada) {
			Bestia besAnterior = bDao.getOne(besActualizada);
			if(besAnterior != null) {
				if(besActualizada.getNombre() == "") {
					besActualizada.setNombre(besAnterior.getNombre());
				}
				if(besActualizada.getPeligrosidad() == "") {
					besActualizada.setPeligrosidad(besAnterior.getPeligrosidad());
				}
				return bDao.update(besActualizada);
			}
			return besAnterior;
}
	}