package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import logic.LogicNoticia;
import entities.Noticia;
import java.util.LinkedList;

/**
 * Servlet implementation class SvNoticia
 */
@WebServlet("/SvNoticia")
public class SvNoticia extends HttpServlet {
	
	//CONSTANTES
	private final String NOTICIA_NOT_FOUND = "No existe una noticia con ese id";
	private final String NOTICIAS_NOT_CREATED = "No existen noticas creadas actualmente";
	
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
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
