package logic;

import java.util.LinkedList;

import data.DataCategoria;
import entities.Categoria;

public class LogicCategoria {
public DataCategoria catDAO = new DataCategoria();
	
	public Categoria getOne(Categoria catE) {
		return catDAO.getOne(catE);
	}
	
	public LinkedList<Categoria> findAll() {
		return catDAO.findAll();
	}
	
	public Categoria save(Categoria catS) {
		return catDAO.save(catS);
	}
	
	public Categoria update(Categoria datos) {
		return catDAO.update(datos);
	}
	
	public Categoria delete(Categoria datoBorrado) {
		Categoria dato = getOne(datoBorrado); // consultar 
		return catDAO.delete(dato);
	}
}
