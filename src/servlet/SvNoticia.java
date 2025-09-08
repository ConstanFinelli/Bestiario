package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import jakarta.servlet.RequestDispatcher;

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
	private final String ID_FORMAT_ERROR = "Error en el formato del id ingresado";
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
		RequestDispatcher rd = request.getRequestDispatcher("noticiaForms.jsp");
		String getOneMsg = "";
		String findAllMsg = "";
		if(id != null) {
			Noticia noticia = new Noticia(Integer.parseInt(id));
			noticia = controlador.getOne(noticia);
			if(noticia != null) {
				getOneMsg = getOneMsg + noticia + "<br><br>";
				
			}else {
				getOneMsg = NOTICIA_NOT_FOUND;
				
			}	
			request.setAttribute("getOneMsg", getOneMsg);
		} else {
			LinkedList<Noticia> noticias = new LinkedList<>();
			noticias = controlador.findAll();
			if (!noticias.isEmpty()) {
				for (Noticia unaNoticia : noticias) {
					findAllMsg = findAllMsg + unaNoticia + "<br><br>";
				} 
			} else {
				findAllMsg = NOTICIAS_NOT_CREATED;
			}
			request.setAttribute("findAllMsg", findAllMsg);
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String titulo = request.getParameter("titulo");
		String contenido = request.getParameter("contenido");
		String estado = request.getParameter("estado");
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd"); 
		String idUsuario = request.getParameter("idUsuario");
		String flag = request.getParameter("flag");
		String saveMsg = "";
		RequestDispatcher rd = request.getRequestDispatcher("noticiaForms.jsp"); 
		
		if (flag.equals("post")) {
			try {

				if (titulo != null && contenido != null && estado != null && idUsuario != null) {
					java.util.Date fechaUtil = new java.util.Date();
					Date fechaPublicacion = new Date(fechaUtil.getTime());
					Noticia noticia = new Noticia(titulo, contenido, estado, fechaPublicacion, idUsuario);
					noticia = controlador.save(noticia);
					if (noticia != null) {
						saveMsg = saveMsg + noticia + "<br><nr>";
					} else { //HECHO especialmente para el caso en el que idUsuario no exista
						saveMsg = CREATE_NOTICIA_ERROR;
					}
				} else {
					saveMsg = CREATE_NOTICIA_ERROR;
				}
			} catch (NumberFormatException e) {
				System.out.println(e.getClass().toString());
				e.getMessage();
				saveMsg = ID_FORMAT_ERROR;
			} finally {
				request.setAttribute("saveMsg", saveMsg);
			} 
		} else if(flag.equals("put")) {
			doPut(request, response);
		} else {
			doDelete(request,response);
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("noticiaForms.jsp");
		String updateMsg = "";
		String id = request.getParameter("id");
		String titulo = request.getParameter("titulo");
		String contenido = request.getParameter("contenido");
		String estado = request.getParameter("estado");
		String idUsuario = request.getParameter("idUsuario");
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd"); 
		if (id != "") {
			try {
				java.util.Date fechaUtil = new java.util.Date();
				Date fechaPublicacion = new Date(fechaUtil.getTime());
				Noticia noticia = new Noticia(Integer.parseInt(id), titulo, contenido, estado, fechaPublicacion, idUsuario);
				System.out.println(noticia);
				noticia = controlador.update(noticia);
				if(noticia != null && noticia.getId()!=0) { //!= 0 previsorio para caso donde idUsario no existe
					updateMsg = updateMsg + noticia + "<br><br>";
				}else{
					updateMsg = NOTICIA_NOT_FOUND;
				}

				
				
			} catch (NumberFormatException e) {
				
				e.getMessage();
				updateMsg = ID_FORMAT_ERROR;
			} finally {
				
				request.setAttribute("updateMsg", updateMsg);
				rd.forward(request, response);
			}
			;
		} else {
			response.getWriter().append("Ingresar un id valido");
			//esto se maneja directamente desde el form del jsp
		}
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		RequestDispatcher rd = request.getRequestDispatcher("noticiaForms.jsp");
		String msgDelete = "";
		Noticia noticia = new Noticia(Integer.parseInt(id));
		noticia = controlador.delete(noticia);
		if(noticia == null) {
			msgDelete = NOTICIA_NOT_FOUND;
		} else {
			msgDelete = "Eliminado: " + noticia + "<br><br>";
		}
		request.setAttribute("deleteMsg", msgDelete);
		rd.forward(request, response);

	}

}
