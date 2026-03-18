package servlet.habitat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCaracteristicaHabitat;

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
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearCaracteristicaHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.CARAC_HABITAT_FORM_JSP(""));
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
		CaracteristicaHabitat ch = new CaracteristicaHabitat(ht.getId(), descripcion);
		ch = controlador.save(ch, ht);
		request.setAttribute("savedCaracteristica", ch);
		rd.forward(request, response);
	}

}
