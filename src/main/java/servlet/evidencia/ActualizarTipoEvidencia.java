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
 * Servlet implementation class ActualizarTipoEvidencia
 */
@WebServlet("/evidencias/actualizarTipoEvidencia")
public class ActualizarTipoEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ActualizarTipoEvidencia.class.getName());
       

    public ActualizarTipoEvidencia() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> errores = new ArrayList<>();
		String id = request.getParameter("id");
		String desc = request.getParameter("descripcion");
		String resourceType = request.getParameter("resourceType");
		TipoEvidencia tipo = null;
		try {
			tipo = new TipoEvidencia(Integer.parseInt(id), desc, resourceType);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear el id recibido en el servlet ActualizarTipoEvidencia", e);
			errores.add("La id recibida no es valida");
		}
		
		try {
			tipo = controlador.update(tipo);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir tipo de evidencia en el servlet ActualizarTipoEvidencia", e);
			errores.add("No se ha podido conseguir el tipo de evidencia a actualizar");
		}
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde");
			request.setAttribute("errorGlobal", errores);
		}
		
		request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=tiposEvidencia").forward(request, response);
	}

}
