package servlet.habitat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicHabitat;

import java.io.IOException;

import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CrearHabitat
 */
@WebServlet("/habitats/crear")
public class CrearHabitat extends HttpServlet {
	private LogicHabitat controladorHabitat = new LogicHabitat();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats");
		String nombre = request.getParameter("nombre");
		String latitud = request.getParameter("latitud");
		String longitud = request.getParameter("longitud");
		String localizacion = request.getParameter("localizacion");
		try{
		Habitat ht = new Habitat(0, nombre, localizacion, Double.parseDouble(latitud), Double.parseDouble(longitud));
		ht = controladorHabitat.save(ht);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error parseando los parámetros del habitat en el servlet CrearHabitat", e);
			request.setAttribute("errorGlobal", "Latitud o longitud inválidos");
			rd.forward(request, response);
			return;
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error creando el habitat en el servlet CrearHabitat", e);
			request.setAttribute("errorGlobal", "No se ha podido crear el habitat");
			rd.forward(request, response);
			return;
		}
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage", "¡Habitat creado con éxito!");
		}
		rd.forward(request, response);
	}

}
