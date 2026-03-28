package logic;

import entities.Noticia;
import java.util.LinkedList;
import entities.Usuario;
import data.DataUsuario;

import data.DataNoticia;

public class LogicNoticia {
	private DataNoticia dataNoticia = new DataNoticia();
	private DataUsuario dataUsuario = new DataUsuario();
	private LogicEmail logicEmail = new LogicEmail();
	
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
		            System.out.println("❌ Falló envío a: " + u.getCorreo());
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
			// if(noticiaNueva.getEstado().equals("")) noticiaNueva.setEstado(noticiaAnterior.getEstado());
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
