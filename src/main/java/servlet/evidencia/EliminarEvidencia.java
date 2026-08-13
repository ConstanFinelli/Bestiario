package servlet.evidencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEvidencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarEvidencia
 */
@WebServlet("/evidencias/eliminar")
public class EliminarEvidencia extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private static final Logger logger = Logger.getLogger(EliminarEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> errores = new ArrayList<>();
		int idTipo = Integer.parseInt(request.getParameter("idTipoEvidencia"));
		int nroEvidencia = Integer.parseInt(request.getParameter("nroEvidencia"));
		String idBestia = request.getParameter("idBestia");
		Evidencia evidencia = null;
		try {
			evidencia = controladorEvidencia.delete(new Evidencia(nroEvidencia, null, null, null, new TipoEvidencia(idTipo)));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al eliminar la evidencia en el servlet EliminarEvidencia", e);
			errores.add("No se ha podido eliminar la evidencia");
		}
		
		if(!errores.isEmpty()) {
			errores.add("");
			request.setAttribute("errorGlobal", errores);
		}
		
		request.setAttribute("deletedEvidencia", evidencia);
		request.getRequestDispatcher(HttpRoutes.OBTENER_REGISTRO_BESTIA("") + "?id="+idBestia).forward(request, response);
	}

}
