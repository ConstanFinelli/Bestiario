package servlet.habitat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCaracteristicaHabitat;
import logic.LogicHabitat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.CaracteristicaHabitat;
import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarCaracteristica
 */
@WebServlet("/habitats/actualizarCaracteristicaHabitat")
public class ActualizarCaracteristica extends HttpServlet {
	private LogicCaracteristicaHabitat controlador = new LogicCaracteristicaHabitat();
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(ActualizarCaracteristica.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarCaracteristica() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=carHabitat");
		List<String> errores = new ArrayList<>();
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		String newDescripcion = request.getParameter("newDescripcion");
		CaracteristicaHabitat ch = null;
		try {
			Habitat ht = new Habitat(Integer.parseInt(id));
			ch = new CaracteristicaHabitat(Integer.parseInt(id), descripcion);
			ht = controladorHabitat.getOne(ht);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error parseando el id del habitat", nfe);
			request.setAttribute("errorGlobal", "Id de habitat invalido");
			rd.forward(request, response);
			return;
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el habitat", e);
			request.setAttribute("errorGlobal", "Error buscando el habitat");
			rd.forward(request, response);
			return;
		}
		
		try {
			ch = controlador.update(ch, newDescripcion);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error actualizando las caracteristicas del habitat", e);
			request.setAttribute("errorGlobal", "Error actualizando las caracteristicas");
			rd.forward(request, response);
			return;
		}
		
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage", "¡Caracteristica del habitat actualizada con éxito!");
		}
		
		rd.forward(request, response);
	}

}
