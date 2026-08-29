package servlet.evidencia;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.LogicEvidencia;
import logic.LogicTipoEvidencia;

/**
 * Servlet implementation class CrearEvidencia
 */
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024 * 2,  // 2MB memoria antes de escribir temporal en disco
	maxFileSize = 1024 * 1024 * 50,       // 50MB máximo por archivo individual
	maxRequestSize = 1024 * 1024 * 100    // 100MB máximo por petición total
)
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
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ACTUALIZACION_REGISTRO_JSP(""));
		Part archivo = request.getPart("archivo");
		String fileId = CloudinaryHelper.upload(archivo);
		String estado = request.getParameter("estado");
		String idTipoEvidencia = request.getParameter("idTipoEvidencia");
		
		LocalDate fechaO = null;
		TipoEvidencia tipo = null;
		Evidencia evidencia = null;
		try {
			tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(idTipoEvidencia)));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el tipo de evidencia en el servlet CrearEvidencia", e);
			request.setAttribute("errorGlobal","Error obteniendo el tipo de evidencia");
			rd.forward(request,response);
			return;
		}
		try {
			 fechaO = LocalDate.parse(request.getParameter("fechaObtencion"));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error parseando la fecha de obtencion en el servlet CrearEvidencia");
			request.setAttribute("errorGlobal","Error leyendo la fecha de obtencion");
			rd.forward(request,response);
			return;
		}
		
		try {
			evidencia = controladorEvidencia.save(new Evidencia(0, fechaO, estado, fileId, tipo));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error guardando la evidencia creada en el servlet CrearEvidencia", e);
			request.setAttribute("errorGlobal","Error guardando la evidencia creada");
			rd.forward(request,response);
			return;
		}
		 
		request.setAttribute("createdEvidencia", evidencia);
		rd.forward(request, response);
	}

}
