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
 * Servlet implementation class CrearEvidencia
 */
@MultipartConfig
@WebServlet("/evidencias/crear")
public class CrearEvidencia extends HttpServlet {
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia(); 
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private static final Logger logger = Logger.getLogger(CrearEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<String> errores = new ArrayList<>();
		Part archivo = request.getPart("archivo");
		String fileId = CloudinaryHelper.upload(archivo);
		String estado = request.getParameter("estado");
		String idTipoEvidencia = request.getParameter("idTipoEvidencia");
		LocalDateTime fechaO = null;
		TipoEvidencia tipo = null;
		Evidencia evidencia = null;
		try {
			tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(idTipoEvidencia)));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el tipo de evidencia en el servlet CrearEvidencia", e);
			errores.add("Error obteniendo el tipo de evidencia");
		}
		try {
			 fechaO = LocalDateTime.parse(request.getParameter("fechaObtencion"));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error parseando la fecha de obtencion en el servlet CrearEvidencia");
			errores.add("Error leyendo la fecha de obtencion");
		}
		
		try {
			evidencia = controladorEvidencia.save(new Evidencia(0, fechaO, estado, fileId, tipo));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error guardando la evidencia creada en el servlet CrearEvidencia", e);
			errores.add("Error guardando la evidencia creada");
		}
		
		if(!errores.isEmpty()) {
			errores.add("");
			request.setAttribute("errorGlobal", errores);
		}
		 
		request.setAttribute("createdEvidencia", evidencia);
		request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP("")).forward(request, response);
	}

}
