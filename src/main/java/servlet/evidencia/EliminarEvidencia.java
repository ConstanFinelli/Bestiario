package servlet.evidencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEvidencia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Evidencia;
import entities.TipoEvidencia;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarEvidencia
 */
@WebServlet("/evidencias/eliminar")
public class EliminarEvidencia extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private static final Logger logger = Logger.getLogger(EliminarEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idBestia = request.getParameter("idBestia");
		int idTipo = 0;
		int nroEvidencia = 0;
		try {
			idTipo = Integer.parseInt(request.getParameter("idTipoEvidencia"));
			nroEvidencia = Integer.parseInt(request.getParameter("nroEvidencia"));
		} catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Parámetros inválidos en el servlet EliminarEvidencia", e);
			request.setAttribute("errorGlobal", "Parámetros de evidencia inválidos.");
			request.getRequestDispatcher(HttpRoutes.OBTENER_REGISTRO_BESTIA("") + "?id=" + idBestia).forward(request, response);
			return;
		}

		Evidencia evidencia = null;
		try {
			evidencia = controladorEvidencia.delete(new Evidencia(nroEvidencia, null, null, null, new TipoEvidencia(idTipo)));
		}catch(DataNotFoundException e) {
			logger.log(Level.WARNING, "Evidencia no encontrada en el servlet EliminarEvidencia", e);
			request.setAttribute("errorGlobal", "La evidencia que se intentó eliminar no existe.");
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al eliminar la evidencia en el servlet EliminarEvidencia", e);
			request.setAttribute("errorGlobal","No se ha podido eliminar la evidencia");
		}
		
		request.setAttribute("deletedEvidencia", evidencia);
		request.getRequestDispatcher(HttpRoutes.OBTENER_REGISTRO_BESTIA("") + "?id="+idBestia).forward(request, response);
	}

}
