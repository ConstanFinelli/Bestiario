package logic;


import data.DataRegistro;
import entities.Registro;
import entities.Bestia;

public class LogicRegistro {
	public DataRegistro rDao = new DataRegistro();
	
	public Registro getLast(Bestia b) {
		return rDao.getLast(b);
	}
	
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
