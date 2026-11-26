package servlet.bestia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicEvidencia;
import logic.LogicRegistro;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Evidencia;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarBestia
 */
@WebServlet("/bestias/eliminar")
public class EliminarBestia extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private static final Logger logger = Logger.getLogger(EliminarBestia.class.getName());

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id));
		try {
			for (Evidencia evidencia : controladorEvidencia.findAllByBestia(bestia)) {
				controladorEvidencia.delete(evidencia);
			}
			controladorRegistro.deleteImages(bestia);
			controlador.delete(bestia);
		}catch(DataNotFoundException e) {
			logger.log(Level.WARNING, "Bestia no encontrada para eliminar en EliminarBestia", e);
			request.setAttribute("errorGlobal", "La bestia que intentó eliminar no existe.");
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error crítico al eliminar bestia en el servlet EliminarBestia", e);
			request.setAttribute("errorGlobal", "No se ha podido eliminar la bestia seleccionada. ");
		}
		request.getRequestDispatcher(HttpRoutes.LISTAR_BESTIAS("")).forward(request, response);		
	}

}
