package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import logic.LogicNoticia;
import entities.Noticia;
import entities.TipoEvidencia;

import java.util.LinkedList;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Servlet implementation class SvNoticia
 */
@WebServlet("/SvNoticia")
public class SvNoticia extends HttpServlet {
	
	//CONSTANTES
	private final String NOTICIA_NOT_FOUND = "No existe una noticia con ese id";
	private final String NOTICIAS_NOT_CREATED = "No existen noticas creadas actualmente";
	private final String CREATE_NOTICIA_ERROR = "Error al crear la nueva noticia. Revisar datos enviados";
	public LogicNoticia controlador = new LogicNoticia();
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvNoticia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	//##GET ONE##
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		if(id != null) {
			Noticia noticia = new Noticia(Integer.parseInt(id));
			noticia = controlador.getOne(noticia);
			if(noticia != null) {
				response.getWriter().append(noticia.toString());
			}else {
				response.getWriter().append(NOTICIA_NOT_FOUND);
			}	
		} else {
			LinkedList<Noticia> noticias = new LinkedList<>();
			noticias = controlador.findAll();
			if (!noticias.isEmpty()) {
				for (Noticia unaNoticia : noticias) {
					response.getWriter().append(unaNoticia.toString());
				} 
			} else {
				response.getWriter().append(NOTICIAS_NOT_CREATED);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String titulo = request.getParameter("titulo");
		String contenido = request.getParameter("contenido");
		String estado = request.getParameter("estado");
		String fecha = request.getParameter("fechaPublicacion");
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd"); 
		String idUsuario = request.getParameter("idUsuario");
		
		try {

			if (titulo != null && contenido != null && estado != null && fecha != null && idUsuario != null) {
				java.util.Date fechaUtil = formatoFecha.parse(fecha);
				System.out.println(fecha);
				Date fechaPublicacion = new Date(fechaUtil.getTime());
				Noticia noticia = new Noticia(titulo,contenido,estado,fechaPublicacion,idUsuario);
				noticia = controlador.save(noticia);
				if (noticia != null) {
					response.getWriter().append(noticia.toString());
				} else { //HECHO especialmente para el caso en el que idUsuario no exista
					response.getWriter().append(CREATE_NOTICIA_ERROR);
				}
			} else {
				response.getWriter().append(CREATE_NOTICIA_ERROR);
			}
		} catch (NumberFormatException | ParseException e) {
			System.out.println(e.getClass().toString());
			e.getMessage();
			if(e instanceof ParseException) {
				response.getWriter().append("Error en el formato de la fecha ingresada");
			}else {
				response.getWriter().append("Error en el formato del id ingresado");
			}
		};
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String titulo = request.getParameter("titulo");
		String contenido = request.getParameter("contenido");
		String estado = request.getParameter("estado");
		String fecha = request.getParameter("fechaPublicacion");
		String idUsuario = request.getParameter("idUsuario");
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd"); 
		if (id != null) {
			try {
				java.util.Date fechaUtil = formatoFecha.parse(fecha);
				Date fechaPublicacion = new Date(fechaUtil.getTime());
				Noticia noticia = new Noticia(Integer.parseInt(id), titulo, contenido, estado, fechaPublicacion, idUsuario);
				noticia = controlador.update(noticia);
				if(noticia != null && noticia.getId()!=0) { //!= 0 previsorio para caso donde idUsario no existe
					response.getWriter().append(noticia.toString());
				}else{
					response.getWriter().append(NOTICIA_NOT_FOUND);
				}

				
				
			} catch (NumberFormatException | ParseException e) {
				System.out.println(e.getClass().toString());
				e.getMessage();
				if (e instanceof ParseException) {
					response.getWriter().append("Error en el formato de la fecha ingresada");
				} else {
					response.getWriter().append("Error en el formato del id ingresado");
				}
			}
			;
		} else {
			response.getWriter().append("Ingresar un id valido");
		}
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if(id != null) {
			Noticia noticia = new Noticia(Integer.parseInt(id));
			noticia = controlador.delete(noticia);
			response.getWriter().append("Datos de la noticia eliminada: \n"+noticia.toString());
		}else {
			response.getWriter().append("Noticia no encontrada");
		}
	}

}
