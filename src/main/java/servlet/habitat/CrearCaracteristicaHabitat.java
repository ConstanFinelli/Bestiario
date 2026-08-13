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

import entities.CaracteristicaHabitat;
import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CrearCaracteristicaHabitat
 */
@WebServlet("/habitats/crearCaracteristicaHabitat")
public class CrearCaracteristicaHabitat extends HttpServlet {
	private LogicCaracteristicaHabitat controlador = new LogicCaracteristicaHabitat();
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(CrearCaracteristicaHabitat.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearCaracteristicaHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=carHabitat");
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		try{
		Habitat ht = new Habitat(Integer.parseInt(id));
		ht = controladorHabitat.getOne(ht);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error parseando la id del habitat en el servlet CrearCaracteristicaHabitat", e);
			request.setAttribute("errorGlobal", "Id del habitat invalida");
			rd.forward(request, response);
			return;
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el habitat en el servlet CrearCaracteristicaHabitat", e);
			request.setAttribute("errorGlobal", "No se ha podido obtener el habitat");
			rd.forward(request, response);
			return;
		}
		try{
		CaracteristicaHabitat ch = new CaracteristicaHabitat(ht.getId(), descripcion);
		ch = controlador.save(ch, ht);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error creando la característica en el servlet CrearCaracteristicaHabitat", e);
			request.setAttribute("errorGlobal", "No se ha podido crear la característica");
			rd.forward(request, response);
			return;
		}
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage", "¡Característica creada con éxito!");
		}
		rd.forward(request, response);
	}

}
