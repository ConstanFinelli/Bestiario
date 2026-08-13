package servlet.habitat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicHabitat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarHabitat
 */
@WebServlet("/habitats/actualizar")
public class ActualizarHabitat extends HttpServlet {
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(ActualizarHabitat.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats");
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String latitud = request.getParameter("latitud");
		String longitud = request.getParameter("longitud");
		String localizacion = request.getParameter("localizacion");
		
		try{
		Habitat ht = new Habitat(Integer.parseInt(id), nombre, localizacion, Double.parseDouble(latitud), Double.parseDouble(longitud));
		ht = controladorHabitat.update(ht);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error parseando los parámetros del habitat en el servlet ActualizarHabitat", e);
			request.setAttribute("errorGlobal", "Id, latitud o longitud inválidos");
			rd.forward(request, response);
			return;
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error actualizando el habitat en el servlet ActualizarHabitat", e);
			request.setAttribute("errorGlobal", "Error actualizando el habitat");
			rd.forward(request, response);
			return;
		}
		

		if(request.getAttribute("errorGlobal") == null) {
		request.setAttribute("feedbackMessage", "¡Habitat actualizado con éxito!");
		}
		rd.forward(request, response);
	}

}
