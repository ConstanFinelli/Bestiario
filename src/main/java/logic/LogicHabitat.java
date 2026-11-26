package logic;

import java.util.LinkedList;

import data.DataHabitat;
import entities.Bestia;
import entities.Habitat;
import exceptions.DataNotFoundException;
import exceptions.DuplicateDataException;

public class LogicHabitat {
	private DataHabitat hDAO = new DataHabitat();
	
	public Habitat getOne(Habitat ht) throws DataNotFoundException {
		return hDAO.getOne(ht);
	}
	
	public LinkedList<Habitat> findAll(){
		return hDAO.findAll();
	}
	
	public LinkedList<Habitat> findAllByBestia(Bestia b){
		return hDAO.findAllByBestia(b);
	}
	
	public Habitat save(Habitat ht) throws DuplicateDataException {
		return hDAO.save(ht);
	}
	
	public Habitat update(Habitat ht) throws DataNotFoundException, DuplicateDataException {
		return hDAO.update(ht);
	}
	
	public Habitat delete(Habitat ht) {
		return hDAO.delete(ht);
	}
}
