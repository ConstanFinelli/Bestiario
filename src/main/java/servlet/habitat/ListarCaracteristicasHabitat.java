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
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=carHabitat");
		Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
		ht = controladorHabitat.getOne(ht);
		LinkedList<CaracteristicaHabitat> hts = new LinkedList<>();
		hts = controlador.findAllById(ht);
		request.getSession().setAttribute("associatedHabitat", ht);
		request.setAttribute("foundCaracteristicas", hts);
		rd.forward(request, response);
		}
}
