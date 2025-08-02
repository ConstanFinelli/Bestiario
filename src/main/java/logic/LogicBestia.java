package logic;

import java.util.LinkedList;

import data.DataBestia;
import entities.Bestia;

public class LogicBestia {
		private DataBestia bestiaDao= new DataBestia();
		
		public Bestia getOne(Bestia bes) {
			return bestiaDao.getOne(bes);
		}
		
		public LinkedList<Bestia> findAll() {
			return bestiaDao.findAll();
		}
		
		public Bestia save(Bestia bes) {
			return bestiaDao.save(bes);
		}
		
		public Bestia delete(Bestia bes) {
			bes = bestiaDao.delete(bes);
			return bes;
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
