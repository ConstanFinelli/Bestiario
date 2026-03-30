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
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats");
		String nombre = request.getParameter("nombre");
		String latitud = request.getParameter("latitud");
		String longitud = request.getParameter("longitud");
		String localizacion = request.getParameter("localizacion");
		Habitat ht = new Habitat(0, nombre, localizacion, Double.parseDouble(latitud), Double.parseDouble(longitud));
		String feedbackMessage = "";
		ht = controladorHabitat.save(ht);
		if(ht == null) {
			feedbackMessage = "¡No se ha podido crear el habitat!";
		}else {
			feedbackMessage = "¡Habitat creada con éxito!";
		}
		request.setAttribute("feedbackMessage", feedbackMessage);
		rd.forward(request, response);
	}

}
