package servlet.noticia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicNoticia;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Noticia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class SvListarNoticias
 */
@WebServlet("/noticias/listar")
public class ListarNoticias extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private LogicNoticia controlador = new LogicNoticia();
	private static final Logger logger = Logger.getLogger(ListarNoticias.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarNoticias() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String flag = request.getParameter("flag");
		
		LinkedList<Noticia> noticias = null;
		
		RequestDispatcher rd = null;
		try {
			if("ultimasNoticias".equals(flag)) { 
				noticias = controlador.getUltimasNoticias();
				rd = request.getRequestDispatcher(HttpRoutes.HOME_JSP(""));
			}else {
				noticias = controlador.findAll();
				rd = request.getRequestDispatcher(HttpRoutes.NOTICIAS_JSP(""));
			}
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error crítico al listar las noticias en servlet ListarNoticias", e);
			request.setAttribute("errorGlobal", "No pudimos conectar con la base de datos. Por favor, intenta más tarde.");
			rd = request.getRequestDispatcher(HttpRoutes.HOME_JSP(""));
			noticias = new LinkedList<>();
		}
		
		request.setAttribute("noticias", noticias);
		rd.forward(request, response);
	}

}
