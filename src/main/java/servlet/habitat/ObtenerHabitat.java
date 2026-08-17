package servlet.habitat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicHabitat;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ObtenerHabitat
 */
@WebServlet("/habitat/obtener")
public class ObtenerHabitat extends HttpServlet {
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(ObtenerHabitat.class.getName());

	private static final long serialVersionUID = 1L;
       
 
    public ObtenerHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.HABITAT_FORM_JSP(""));
		String id = request.getParameter("id");
		Habitat ht = null;
		try{
			ht = new Habitat(Integer.parseInt(id));
			ht = controladorHabitat.getOne(ht);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error parseando la id del hábitat en el servlet ObtenerHabitat", e);
			request.setAttribute("errorGlobal", "Id del hábitat invalida");
			rd.forward(request, response);
			return;
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el hábitat en el servlet ObtenerHabitat", e);
			request.setAttribute("errorGlobal", "No se ha podido obtener el hábitat");
			rd.forward(request, response);
			return;
		}
		request.setAttribute("gottenHabitat", ht);
		rd.forward(request, response);
	}

}


