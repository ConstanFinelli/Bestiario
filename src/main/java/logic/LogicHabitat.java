package logic;

import java.util.LinkedList;

import data.DataHabitat;
import entities.Habitat;

public class LogicHabitat {
	private DataHabitat hDAO = new DataHabitat();
	
	public Habitat getOne(Habitat ht) {
		return hDAO.getOne(ht);
	}
	
	public LinkedList<Habitat> findAll(){
		return hDAO.findAll();
	}
	
	public Habitat save(Habitat ht) {
		return hDAO.save(ht);
	}
	
	public Habitat update(Habitat ht) {
		return hDAO.update(ht);
	}
	
	public Habitat delete(Habitat ht) {
		return hDAO.delete(ht);
	}
}
