package servlet.evidencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.LogicEvidencia;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarEvidencia
 */
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024 * 2,  // 2MB memoria antes de escribir temporal en disco
	maxFileSize = 1024 * 1024 * 50,       // 50MB máximo por archivo individual
	maxRequestSize = 1024 * 1024 * 100    // 100MB máximo por petición total
)
@WebServlet("/evidencias/actualizar")
public class ActualizarEvidencia extends HttpServlet {
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private static final Logger logger = Logger.getLogger(ActualizarEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarEvidencia() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> errores = new ArrayList<>();
		TipoEvidencia tipo = null;
		Evidencia evidencia = null;
		try {
			tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(request.getParameter("idTipo"))));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener el tipo de evidencia en el servlet ActualizarEvidencia", e);
			errores.add("No se ha podido conseguir el tipo de evidencia de la evidencia a actualizar");
		}
		Part archivo = request.getPart("archivo");
		String nroEvidencia = request.getParameter("nroEvidencia");
		String fileId = CloudinaryHelper.upload(archivo);
		try {
			evidencia = controladorEvidencia.update(new Evidencia(Integer.parseInt(nroEvidencia), LocalDateTime.parse(request.getParameter("fechaObtencion")), request.getParameter("estado"), fileId , tipo));
			}catch(Exception e) {
				logger.log(Level.WARNING, "Error al actualizar la evidencia en el servlet ActualizarEvidencia", e);
				errores.add("No se ha podido actualizar la evidencia a actualizar");
			}
		if(!errores.isEmpty()) {
			errores.add("");
			request.setAttribute("errorGlobal", errores);
		}
		request.setAttribute("updatedEvidencia", evidencia);
		request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP("")).forward(request, response);
	}

}
