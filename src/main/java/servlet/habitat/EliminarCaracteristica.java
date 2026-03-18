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
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarCaracteristica
 */
@WebServlet("/habitats/eliminarCaracteristica")
public class EliminarCaracteristica extends HttpServlet {
	private LogicCaracteristicaHabitat controlador = new LogicCaracteristicaHabitat();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarCaracteristica() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.CARAC_HABITAT_FORM_JSP(""));
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		CaracteristicaHabitat ch = new CaracteristicaHabitat(Integer.parseInt(id), descripcion);
		ch = controlador.delete(ch);
		request.setAttribute("deletedCaracteristica", ch);
		rd.forward(request, response);
	}

}
