package servlet.evidencia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarTipoEvidencia
 */
@WebServlet("/evidencias/eliminarTipoEvidencia")
public class EliminarTipoEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	private static final Logger logger = Logger.getLogger(EliminarTipoEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarTipoEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=tiposEvidencia");
		String id = request.getParameter("id");
		TipoEvidencia tipo = null;
		try {
			tipo = new TipoEvidencia(Integer.parseInt(id));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al parsear la id del tipo de Evidencia en el servlet EliminarTipoEvidencia", e);
			request.setAttribute("errorGlobal", "Id del Tipo de evidencia invalida");
			rd.forward(request, response);
			return;
		}
		
		try {
			tipo = controlador.delete(tipo);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error eliminando el tipo de evidencia en el servlet EliminarTipoEvidencia", e);
			request.setAttribute("errorGlobal", "No se ha podido eliminar el Tipo de evidencia");
			rd.forward(request, response);
			return;
		}
		
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage", "¡Tipo de evidencia eliminada con éxito!");
		}
		
		rd.forward(request, response);
	}

}
