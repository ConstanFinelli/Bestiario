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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.CaracteristicaHabitat;
import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarCaracteristica
 */
@WebServlet("/habitats/actualizarCaracteristicaHabitat")
public class ActualizarCaracteristica extends HttpServlet {
	private LogicCaracteristicaHabitat controlador = new LogicCaracteristicaHabitat();
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(ActualizarCaracteristica.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarCaracteristica() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=carHabitat");
		List<String> errores = new ArrayList<>();
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		String newDescripcion = request.getParameter("newDescripcion");
		CaracteristicaHabitat ch = null;
		try {
			Habitat ht = new Habitat(Integer.parseInt(id));
			ch = new CaracteristicaHabitat(Integer.parseInt(id), descripcion);
			ht = controladorHabitat.getOne(ht);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error parseando el id del habitat", nfe);
			errores.add("Id de habitat invalido");
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el habitat", e);
			errores.add("Error buscando el habitat");
		}
		
		try {
			ch = controlador.update(ch, newDescripcion);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error actualizando las caracteristicas del habitat", e);
			errores.add("Error cargando las caracteristicas");
		}
		
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde");
			request.setAttribute("errorGlobal", errores);
		}
		
		rd.forward(request, response);
	}

}
