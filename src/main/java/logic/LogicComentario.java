package logic;

import java.time.LocalDate;
import java.util.LinkedList;
import entities.Bestia;
import data.DataComentario;
import entities.Comentario;

public class LogicComentario {
	public DataComentario cDao = new DataComentario();
	
	public Comentario getOne(Comentario c) {
		return cDao.getOne(c);
	}
	
	public LinkedList<Comentario> findAll() {
		return cDao.findAll();
	}
	
	public LinkedList<Comentario> findAllByBestia(Bestia b){
		return cDao.findAllByBeast(b);
	}
	
	public Comentario save(Comentario c) {
		c.setFecha(LocalDate.now());
		return cDao.save(c);
	}
	
	public Comentario update(Comentario c) {
		LocalDate oldDate = c.getFecha();
		c.setFecha(LocalDate.now());
		return cDao.update(c, oldDate);
	}
	
	public Comentario delete(Comentario c) {
		return cDao.delete(c);
	}
}
