package servlet.evidencia;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Evidencia;
import entities.TipoEvidencia;
import entities.Usuario;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import logic.LogicBestia;
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
	private LogicBestia controladorBestia = new LogicBestia();
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
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTRO_JSP(""));
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("user");
		String fechaStr = request.getParameter("fechaObtencion");
		Part archivoPart = request.getPart("archivo");
		String idTipoEvidencia = request.getParameter("tipo");
		String idBestia = request.getParameter("idBestia");
		Bestia bestia = null;
		try{
			bestia = new Bestia(Integer.parseInt(idBestia));
			bestia = controladorBestia.getOne(bestia);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear idBestia en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "El id de la bestia es inválido.");
			return;
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir bestia en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "No se ha conseguido la bestia. ");
			return;
		}
		
		LocalDate fecha = null;
		TipoEvidencia te = null;
		Evidencia evidencia = null;
		try {
			te = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(idTipoEvidencia)));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error obteniendo el tipo de evidencia en el servlet CrearEvidencia", e);
			request.setAttribute("errorGlobal","Error obteniendo el tipo de evidencia");
			rd.forward(request,response);
			return;
		}
		try {
			 fecha = LocalDate.parse(request.getParameter("fechaObtencion"));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error parseando la fecha de obtencion en el servlet CrearEvidencia");
			request.setAttribute("errorGlobal","Error leyendo la fecha de obtencion");
			rd.forward(request,response);
			return;
		}
		
		if (fechaStr != null && te != null && archivoPart != null && archivoPart.getSize() > 0) {
		    
		    String archivoId = CloudinaryHelper.upload(archivoPart);

		    try {
		        String estadoEvidencia = (usuario != null && "investigador".equals(usuario.getEstado())) ? "aprobado" : "pendiente";
		        
		        evidencia = new Evidencia(0, fecha, estadoEvidencia, archivoId, te);
		        controladorEvidencia.save(evidencia);
		        
		        LinkedList<Evidencia> evidencias = bestia.getEvidencias();
		        evidencias.add(evidencia);
		        
		        bestia.setEvidencias(evidencias);
		        controladorBestia.saveEvidencias(bestia);

		    } catch (NumberFormatException e) {
		        logger.log(Level.SEVERE, "Error al recibir el numero de tipo de evidencia en el servlet ActualizarRegistro");
		        request.setAttribute("errorGlobal", "Tipo de Evidencia Invalido");
		        doGet(request, response);
		        return;
		    } catch (DateTimeParseException ex) {
		        logger.log(Level.SEVERE, "Error al parsear la fecha de obtencion de la evidencia en el servlet ActualizarRegistro");
		        request.setAttribute("errorGlobal", "Fecha Invalida");
		        doGet(request, response);
		        return;
		    }
		}
		
		response.sendRedirect(HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) + "?id=" + idBestia);
	}

}
