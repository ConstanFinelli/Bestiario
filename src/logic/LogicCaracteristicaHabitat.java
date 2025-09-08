package logic;

import java.util.LinkedList;
import data.DataCaracteristicaHabitat;
import data.DataHabitat;
import entities.CaracteristicaHabitat;
import entities.Habitat;


public class LogicCaracteristicaHabitat {
	private DataCaracteristicaHabitat carDAO = new DataCaracteristicaHabitat();
	private DataHabitat htDAO = new DataHabitat();
	
	public LinkedList<CaracteristicaHabitat> findAllById(Habitat ht) {
		return carDAO.findAllById(ht);
	}
	
	public CaracteristicaHabitat save(CaracteristicaHabitat ch, Habitat ht) {
		if(htDAO.getOne(ht) == null) {
			return null;
		}
		return carDAO.save(ch, ht);
	}
	
	public CaracteristicaHabitat delete(CaracteristicaHabitat ch) {
		return carDAO.delete(ch);
	}
	
	public CaracteristicaHabitat update(CaracteristicaHabitat ch, String newDescripcion) {
		return carDAO.update(ch, newDescripcion);
	}
}
