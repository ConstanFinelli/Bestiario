package servlet.registro;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Bestia;
import entities.Registro;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ObtenerBestia
 */
@WebServlet("/registros/obtenerRegistroConBestiaYFecha")
public class ObtenerRegistroConBestiaYFecha extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private static final Logger logger = Logger.getLogger(ObtenerRegistroConBestiaYFecha.class.getName());
	
	private static final long serialVersionUID = 1L;
     
    public ObtenerRegistroConBestiaYFecha() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTRO_JSP(""));
		String id = request.getParameter("id");
		try{
			Bestia bestia = new Bestia(Integer.parseInt(id));
			bestia = controlador.getOne(bestia);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear id en el servlet ObtenerRegistroConBestiaYFecha", e);
			request.setAttribute("errorGlobal", "La id de la bestia es inválida. ");
			return;
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir bestia en el servlet ObtenerRegistroConBestiaYFecha", e);
			request.setAttribute("errorGlobal", "No se ha conseguido la bestia. ");
			return;
		}
		String fecha = request.getParameter("fecha");
		Registro registro = null;
		
		if(fecha != null) {
			try{
				LocalDateTime fechaParseada = LocalDateTime.parse(fecha);
				registro = controladorRegistro.getRegistroToShow(bestia, fechaParseada);
			}catch(DateTimeParseException e) {
				logger.log(Level.WARNING, "Error al parsear fecha en el servlet ObtenerRegistroConBestiaYFecha", e);
				request.setAttribute("errorGlobal", "La fecha es inválida. ");
				return;
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al conseguir registro en el servlet ObtenerRegistroConBestiaYFecha", e);
				request.setAttribute("errorGlobal", "No se ha conseguido el registro. ");
				return;
			}
		}else {
			try{
				registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al conseguir registro en el servlet ObtenerRegistroConBestiaYFecha", e);
				request.setAttribute("errorGlobal", "No se ha conseguido el registro. ");
				return;
			}
		}

		String UrlImagen = null;
		try{
			UrlImagen = CloudinaryHelper.getImagenRegistro(controladorRegistro.getImagen(bestia, LocalDateTime.now()));
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir imagen en el servlet ObtenerRegistroConBestiaYFecha", e);
			request.setAttribute("errorGlobal", "No se ha conseguido la imagen del registro. ");
		}	
		request.setAttribute("UrlImagen", UrlImagen);
		request.setAttribute("foundBestia", bestia);
		request.setAttribute("foundRegistro", registro);
		rd.forward(request, response);
	}

}
