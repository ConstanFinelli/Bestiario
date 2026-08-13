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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarEvidenciasTipo
 */
@WebServlet("/evidencias/listarPorTipo")
public class ListarEvidenciasTipo extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	private static final Logger logger = Logger.getLogger(ListarEvidenciasTipo.class.getName()); 
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarEvidenciasTipo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP(""));
		List<String> errores = new ArrayList<>();
		String idTipo = request.getParameter("idTipo");
		TipoEvidencia tipo = null;
		LinkedList<Evidencia> evidencias = null;
		try {
			tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(idTipo)));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error buscando el tipo de evidencia en el servlet ListarEvidenciasTipo", e);
			errores.add("No se ha encontrado el tipo de evidencia");
		}
		if(tipo != null) {
			try {
				evidencias = controladorEvidencia.findAllType(tipo);
				request.setAttribute("gottenEvidencias", evidencias);
			}catch(Exception e) {
				logger.log(Level.WARNING, "Error buscando las evidencias del tipo seleccionado en el servlet ListarEvidenciasTipo", e);
				errores.add("No se ha podido traer evidencias del tipo seleccionado");
			}
		}
		
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde");
			request.setAttribute("errorGlobal", errores);
		}
		
		rd.forward(request, response);
	}


}
