package servlet.evidencia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEvidencia;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ObtenerEvidencia
 */
@WebServlet("/evidencias/obtener")
public class ObtenerEvidencia extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	private static final Logger logger = Logger.getLogger(ObtenerEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObtenerEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP(""));
		List<String> errores = new ArrayList<>();
		String idTipo = request.getParameter("IdTipo");
		String nroEvidencia = request.getParameter("nroEvidencia");
		TipoEvidencia t = null;
		Evidencia ev = null;
		try {
			t = new TipoEvidencia(Integer.parseInt(idTipo));
			t = controladorTipoEvidencia.getOne(t);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error parseando el idTipo en el servlet ObtenerEvidencia", nfe);
			errores.add("Id del tipo de evidencia invalida");
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el Tipo de evidencia en el servlet ObtenerEvidencia", e);
			errores.add("No se ha podido encontrar el tipo de evidencia");
		}
		try {
		ev = new Evidencia(Integer.parseInt(nroEvidencia), null, null, null, t);
		ev = controladorEvidencia.getOne(ev);
		request.setAttribute("gottenEvidencia", ev);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error parseando el nroEvidencia en el servlet ObtenerEvidencia", nfe);
			errores.add("Numero de evidencia invalida");
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el Tipo de evidencia en el servlet ObtenerEvidencia", e);
			errores.add("No se ha podido encontrar el tipo de evidencia");
		}
		if(!errores.isEmpty()) {
			errores.add("");
			request.setAttribute("errorGlobal", errores);
		}
		rd.forward(request, response);
	}


}
