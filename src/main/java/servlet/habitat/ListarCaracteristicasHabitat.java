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
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.CaracteristicaHabitat;
import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarCaracteristicasHabitat
 */
@WebServlet("/habitats/listarCaracteristicasHabitat")
public class ListarCaracteristicasHabitat extends HttpServlet {
	private LogicCaracteristicaHabitat controlador = new LogicCaracteristicaHabitat();
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(ListarCaracteristicasHabitat.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarCaracteristicasHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Habitat ht = null;
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=carHabitat");

		try{
			ht = new Habitat(Integer.parseInt(id));
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear id en el servlet ListarCaracteristicasHabitat", e);
			request.setAttribute("errorGlobal", "El id del habitat no es válido. ");
			rd.forward(request, response);
			return;
		}

		try {
			ht = controladorHabitat.getOne(ht);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir habitat en el servlet ListarCaracteristicasHabitat", e);
			request.setAttribute("errorGlobal", "No se ha podido conseguir el habitat. ");
			rd.forward(request, response);
			return;
		}

		LinkedList<CaracteristicaHabitat> hts = new LinkedList<>();
		try {
			hts = controlador.findAllById(ht);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir caracteristicas de habitat en el servlet ListarCaracteristicasHabitat", e);
			request.setAttribute("errorGlobal", "No se han podido conseguir las caracteristicas del habitat. ");
			rd.forward(request, response);
			return;
		}
		request.getSession().setAttribute("associatedHabitat", ht);
		request.setAttribute("foundCaracteristicas", hts);
		rd.forward(request, response);
		}
}
