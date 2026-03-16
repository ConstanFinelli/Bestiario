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
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.HABITAT_FORM_JSP(""));
		String nombre = request.getParameter("nombre");
		String localizacion = request.getParameter("localizacion");
		Habitat ht = new Habitat(0, nombre, null, localizacion);
		ht = controladorHabitat.save(ht);
		request.setAttribute("habitatCreado", ht);
		rd.forward(request, response);
	}

}
