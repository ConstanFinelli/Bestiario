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

import entities.CaracteristicaHabitat;
import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarCaracteristica
 */
@WebServlet("/habitats/eliminarCaracteristicaHabitat")
public class EliminarCaracteristica extends HttpServlet {
	private LogicCaracteristicaHabitat controlador = new LogicCaracteristicaHabitat();
	private LogicHabitat controladorHabitat = new LogicHabitat();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarCaracteristica() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=carHabitat");
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		String feedbackMessage = "";
		Habitat ht = new Habitat(Integer.parseInt(id));
		ht = controladorHabitat.getOne(ht);
		CaracteristicaHabitat ch = new CaracteristicaHabitat(Integer.parseInt(id), descripcion);
		ch = controlador.delete(ch);
		if(ch == null) {
			feedbackMessage = "¡No se ha podido eliminar la característica!";
		}else {
			feedbackMessage = "¡Característica eliminada con éxito!";
		}
		request.setAttribute("feedbackMessage", feedbackMessage);
		rd.forward(request, response);
	}

}
