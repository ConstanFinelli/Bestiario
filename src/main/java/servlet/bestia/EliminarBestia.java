package servlet.bestia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarBestia
 */
@WebServlet("/bestias/eliminar")
public class EliminarBestia extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private static final Logger logger = Logger.getLogger(EliminarBestia.class.getName());

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id));
		try {
			bestia = controlador.delete(bestia);
		}catch(DataNotFoundException e) {
			logger.log(Level.WARNING, "Bestia no encontrada para eliminar en EliminarBestia", e);
			request.getSession().setAttribute("errorGlobal", "La bestia que intentó eliminar no existe.");
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error crítico al eliminar bestia en el servlet EliminarBestia", e);
			request.getSession().setAttribute("errorGlobal", "No se ha podido eliminar la bestia seleccionada. ");
		}
		request.setAttribute("deletedBestia", bestia);
		response.sendRedirect(HttpRoutes.LISTAR_BESTIAS(request.getContextPath()));		
		
	}

}
