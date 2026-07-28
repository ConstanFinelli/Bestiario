package logic;

import entities.Noticia;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Usuario;
import data.DataUsuario;
import data.DbConnector;
import data.DataNoticia;

public class LogicNoticia {
	private DataNoticia dataNoticia = new DataNoticia();
	private DataUsuario dataUsuario = new DataUsuario();
	private LogicEmail logicEmail = new LogicEmail();
	private static final Logger logger = Logger.getLogger(DbConnector.class.getName());
	
	public Noticia getOne(Noticia noticia) {
		return(dataNoticia.getOne(noticia));
	} 
	
	public LinkedList<Noticia> findAll() {
		return(dataNoticia.findAll());
	}
	
	public LinkedList<Noticia> getUltimasNoticias(){
		return(dataNoticia.getUltimasNoticias());
	}

	public Noticia save(Noticia noticia) {
		Noticia newNoticia = dataNoticia.save(noticia);
		
		LinkedList<Usuario> usuarios = dataUsuario.findByRecibirNotifcaciones();
		
		new Thread(() -> {
		    for (Usuario u : usuarios) {
		        try {
		            logicEmail.notificarNuevaNoticia(
		                u.getCorreo(),
		                noticia.getTitulo()
		            );
		        } catch (Exception e) {
		            logger.log(Level.WARNING,"Falló envío a: " + u.getCorreo());
		        }
		    }
		}).start();
		
		return newNoticia;
	}

	public Noticia update(Noticia noticiaNueva) {
		Noticia noticiaAnterior = dataNoticia.getOne(noticiaNueva);
		if(noticiaAnterior != null) {
			if(noticiaNueva.getContenido().equals("")) noticiaNueva.setContenido(noticiaAnterior.getContenido());
			if(noticiaNueva.getTitulo().equals("")) noticiaNueva.setTitulo(noticiaAnterior.getTitulo());
			if(noticiaNueva.getFechaPublicacion() == null) noticiaNueva.setFechaPublicacion(noticiaAnterior.getFechaPublicacion());
			if(noticiaNueva.getPublicador() == null) noticiaNueva.setPublicador(noticiaAnterior.getPublicador());
;			noticiaNueva = dataNoticia.update(noticiaNueva);
			
		} else {
			return null;
		}
		return noticiaNueva;
	}

	public Noticia delete(Noticia noticia) {		
		Noticia deletedNoticia = dataNoticia.getOne(noticia);
		if(deletedNoticia != null) {
			dataNoticia.delete(deletedNoticia);
		}
		return (deletedNoticia);
	}
}
