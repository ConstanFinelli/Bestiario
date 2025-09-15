package logic;

import java.util.LinkedList;

import data.DataRegistro;
import entities.Registro;

public class LogicRegistro {
	public DataRegistro rDao = new DataRegistro();
	
	public Registro save(Registro r) {
		return rDao.save(r);
	}
	
	public Registro update(Registro r) {
		return rDao.update(r);
	}
	
	public Registro delete(Registro r) {
		return rDao.delete(r);
	}
}
