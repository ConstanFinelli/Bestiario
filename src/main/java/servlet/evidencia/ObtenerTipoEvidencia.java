package servlet.evidencia;

import jakarta.servlet.RequestDispatcher;
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
 * Servlet implementation class ObtenerTipoEvidencia
 */
@WebServlet("/evidencias/obtenerTipoEvidencia")
public class ObtenerTipoEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	private static final Logger logger = Logger.getLogger(ObtenerTipoEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObtenerTipoEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.TIPO_EVIDENCIA_FORM_JSP(""));
		String id = request.getParameter("id");
		List<String> errores = new ArrayList<>();
		try {
			TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id));
			tipo = controlador.getOne(tipo);
			request.setAttribute("gottenTipo", tipo);	
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error parseando el id del tipo de evidencia", nfe);
			errores.add("Id invalido");
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error buscando el tipo de evidencia", e);
			errores.add("No se ha podido encontrar el tipo de evidencia");
		}
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde");
			request.setAttribute("errorGlobal", errores);
		}
		rd.forward(request, response);
	}

}
