package logic;


import java.time.LocalDate;

import data.DataRegistro;
import entities.Registro;
import entities.Bestia;

public class LogicRegistro {
	public DataRegistro rDao = new DataRegistro();
	
	public Registro getRegistroToShow(Bestia b, LocalDate fecha) {
		return rDao.getRegistroToShow(b, fecha);
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
