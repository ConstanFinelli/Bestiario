package logic;

import java.util.LinkedList;

import data.DataBestia;
import entities.Bestia;

public class LogicBestia {
	public DataBestia bDao = new DataBestia();
	
	public Bestia getOne(Bestia b) {
		return bDao.getOne(b);
	}
	
	public LinkedList<Bestia> findAll() {
		return bDao.findAll();
	}
	
	public Bestia save(Bestia b) {
		return bDao.save(b);
	}
	
	public Bestia delete(Bestia b) {
		return bDao.delete(b);
	}
	
		public Bestia update(Bestia besActualizada) {
			Bestia besAnterior = bestiaDao.getOne(besActualizada);
			if(besAnterior != null) {
				if(besActualizada.getNombre() == "") {
					besActualizada.setNombre(besAnterior.getNombre());
				}
				if(besActualizada.getPeligrosidad() == "") {
					besActualizada.setPeligrosidad(besAnterior.getPeligrosidad());
				}
				return bestiaDao.update(besActualizada);
			}
			return besAnterior;
}
	}