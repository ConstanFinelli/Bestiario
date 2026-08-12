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
 * Servlet implementation class CrearTipoEvidencia
 */
@WebServlet("/evidencias/crearTipoEvidencia")
public class CrearTipoEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	private static final Logger logger = Logger.getLogger(CrearTipoEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearTipoEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> errores = new ArrayList<>();
		String desc = request.getParameter("descripcion");
		String resourceType = request.getParameter("resourceType");
		TipoEvidencia tipo = new TipoEvidencia(desc, resourceType);
		try {
			tipo = controlador.save(tipo);	
		}catch(Exception e) {
			logger.log(Level.WARNING, "No se ha podido guardar el tipo de evidencia creado en el servlet CrearTipoEvidencia", e);
			errores.add("No se ha podido guardar el tipo creado");
		}
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde");
			request.setAttribute("errorGlobal", errores);
		}
			
		request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=tiposEvidencia").forward(request, response);
	}
}