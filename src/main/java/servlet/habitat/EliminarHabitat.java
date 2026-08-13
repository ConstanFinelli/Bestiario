package servlet.habitat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicHabitat;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.IOException;

import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarHabitat
 */
@WebServlet("/habitats/eliminar")
public class EliminarHabitat extends HttpServlet {
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(EliminarHabitat.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarHabitat() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats");
		String id = request.getParameter("id");
		try{
		Habitat ht = new Habitat(Integer.parseInt(id));
		ht = controladorHabitat.delete(ht);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error parseando la id del habitat en el servlet EliminarHabitat", e);
			request.setAttribute("errorGlobal", "Id del habitat invalida");
			rd.forward(request, response);
			return;
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error eliminando el habitat en el servlet EliminarHabitat", e);
			request.setAttribute("errorGlobal", "No se ha podido eliminar el habitat");
			rd.forward(request, response);
			return;
		}
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage", "El habitat se ha eliminado correctamente");
		}

		rd.forward(request, response);
	}

}
