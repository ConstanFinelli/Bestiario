package logic;

import java.util.LinkedList;
import data.DataTipoEvidencia;
import entities.TipoEvidencia;
import exceptions.DataNotFoundException;

public class LogicTipoEvidencia {
	public DataTipoEvidencia teDAO = new DataTipoEvidencia();
	
	public TipoEvidencia getOne(TipoEvidencia tipoE) throws DataNotFoundException {
		return teDAO.getOne(tipoE);
	}
	
	public LinkedList<TipoEvidencia> findAll() {
		return teDAO.findAll();
	}
	
	public TipoEvidencia save(TipoEvidencia tipoS) {
		return teDAO.save(tipoS);
	}
	
	public TipoEvidencia update(TipoEvidencia datos) throws DataNotFoundException {
		return teDAO.update(datos);
	}
	
	public TipoEvidencia delete(TipoEvidencia datoBorrado) throws DataNotFoundException {
		TipoEvidencia dato = getOne(datoBorrado); // consultar 
		return teDAO.delete(dato);
	}
}
