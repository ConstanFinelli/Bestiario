package logic;

import java.time.LocalDateTime;
import java.util.LinkedList;
import entities.Bestia;
import data.DataComentario;
import entities.Comentario;
import exceptions.DataNotFoundException;

public class LogicComentario {
	public DataComentario cDao = new DataComentario();
	
	public Comentario getOne(Comentario c) throws DataNotFoundException {
		return cDao.getOne(c);
	}
	
	public LinkedList<Comentario> findAllByBestia(Bestia b){
		return cDao.findAllByBestia(b);
	}
	
	public Comentario save(Comentario c) {
		c.setFecha(LocalDateTime.now());
		return cDao.save(c);
	}
	
	public Comentario update(Comentario c) throws DataNotFoundException {
		LocalDateTime oldDate = c.getFecha();
		c.setFecha(LocalDateTime.now());
		return cDao.update(c, oldDate);
	}
	
	public Comentario delete(Comentario c) {
		return cDao.delete(c);
	}
}
