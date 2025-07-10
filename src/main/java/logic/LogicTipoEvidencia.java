package logic;

import java.util.LinkedList;
import data.DataTipoEvidencia;
import entities.TipoEvidencia;

public class LogicTipoEvidencia {
	public DataTipoEvidencia teDAO = new DataTipoEvidencia();
	
	public TipoEvidencia getOne(TipoEvidencia tipoE) {
		return teDAO.getOne(tipoE);
	}
	
	public LinkedList<TipoEvidencia> findAll() {
		return teDAO.findAll();
	}
	
	public TipoEvidencia save(TipoEvidencia tipoS) {
		return teDAO.save(tipoS);
	}
	
	public TipoEvidencia update(TipoEvidencia datos) {
		return teDAO.update(datos);
	}
	
	public TipoEvidencia delete(TipoEvidencia datoBorrado) {
		return teDAO.delete(datoBorrado);
	}
}
