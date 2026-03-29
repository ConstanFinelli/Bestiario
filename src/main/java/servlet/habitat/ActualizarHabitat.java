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
 * Servlet implementation class ActualizarHabitat
 */
@WebServlet("/habitats/actualizar")
public class ActualizarHabitat extends HttpServlet {
	private LogicHabitat controladorHabitat = new LogicHabitat();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats");
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String localizacion = request.getParameter("localizacion");
		String feedbackMessage = "";
		
		Habitat ht = new Habitat(Integer.parseInt(id), nombre, null, localizacion);
		ht = controladorHabitat.update(ht);
		if(ht == null) {
			feedbackMessage = "¡No se ha podido actualizar el habitat!";
		}else {
			feedbackMessage = "¡Habitat actualizada con éxito!";
		}
		request.setAttribute("feedbackMessage", feedbackMessage);
		rd.forward(request, response);
	}

}
