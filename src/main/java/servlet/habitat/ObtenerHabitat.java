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
 * Servlet implementation class ObtenerHabitat
 */
@WebServlet("/habitat/obtener")
public class ObtenerHabitat extends HttpServlet {
	private LogicHabitat controladorHabitat = new LogicHabitat();
	
	private static final long serialVersionUID = 1L;
       
 
    public ObtenerHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.HABITAT_FORM_JSP(""));
		String id = request.getParameter("id");
		Habitat ht = new Habitat(Integer.parseInt(id));
		ht = controladorHabitat.getOne(ht);
		request.setAttribute("gottenHabitat", ht);
		rd.forward(request, response);
	}

}


