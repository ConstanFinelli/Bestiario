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
 * Servlet implementation class EliminarHabitat
 */
@WebServlet("/habitats/eliminar")
public class EliminarHabitat extends HttpServlet {
	private LogicHabitat controladorHabitat = new LogicHabitat();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.HABITAT_FORM_JSP(""));
		String id = request.getParameter("id");
		Habitat ht = new Habitat(Integer.parseInt(id));
		ht = controladorHabitat.delete(ht);
		request.setAttribute("deletedHabitat", ht);
		rd.forward(request, response);
	}

}
