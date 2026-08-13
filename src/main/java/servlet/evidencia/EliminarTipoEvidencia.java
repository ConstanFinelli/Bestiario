package servlet.evidencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		List<String> errores = new ArrayList<>();
		String id = request.getParameter("id");
		TipoEvidencia tipo = null;
		try {
			tipo = new TipoEvidencia(Integer.parseInt(id));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al parsear la id del tipo de Evidencia en el servlet EliminarTipoEvidencia", e);
			errores.add("Id del Tipo de evidencia invalidad");
		}
		
		try {
			tipo = controlador.delete(tipo);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error eliminando el tipo de evidencia en el servlet EliminarTipoEvidencia", e);
			errores.add("No se ha podido eliminar el Tipo de evidencia");
		}
		
		if(!errores.isEmpty()) {
			errores.add("");
			request.setAttribute("errorGlobal", errores);
		}
		
		request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=tiposEvidencia").forward(request, response);
	}

}
