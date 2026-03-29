package servlet.habitat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCaracteristicaHabitat;

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
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.CARAC_HABITAT_FORM_JSP(""));
		Habitat ht = new Habitat(Integer.parseInt(id));
		LinkedList<CaracteristicaHabitat> hts = new LinkedList<>();
		hts = controlador.findAllById(ht);
		request.setAttribute("foundCaracteristicas", hts);
		rd.forward(request, response);
		}
}
