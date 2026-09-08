package servlet.noticia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Noticia;
import helpers.HttpRoutes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicNoticia;

/**
 * Servlet implementation class EliminarNoticia
 */
@WebServlet("/noticias/eliminar")
public class EliminarNoticia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicNoticia controlador = new LogicNoticia();
	private static final Logger logger = Logger.getLogger(EliminarNoticia.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarNoticia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("idNoticia");
		Noticia noticia = new Noticia();
		try {
			noticia.setId(Integer.parseInt(id));
			controlador.delete(noticia);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error parseando el id ingresado en el servlet EliminarNoticia", nfe);
			request.setAttribute("errorGlobal", "La id ingresada no es valida");
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al eliminar la noticia en el servlet EliminarNoticia", e);
			request.setAttribute("errorGlobal", "No se ha podido eliminar la noticia");
		}
		request.getRequestDispatcher(HttpRoutes.LISTAR_NOTICIAS("")).forward(request, response);
		
	}

}
