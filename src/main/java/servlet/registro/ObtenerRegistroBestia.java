package servlet.registro;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Registro;
import helpers.CloudinaryHelper;
import helpers.EnvHelper;
import helpers.HttpRoutes;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

/**
 * Servlet implementation class ObtenerBestia
 */
@WebServlet("/registros/obtenerRegistroConBestia")
public class ObtenerRegistroBestia extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private static final Logger logger = Logger.getLogger(ObtenerRegistroBestia.class.getName());
	
	private static final long serialVersionUID = 1L;
     
    public ObtenerRegistroBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTRO_JSP(""));
		String id = request.getParameter("id");
		String fecha = request.getParameter("fecha");
		Bestia bestia = null;
		String imagen = null;
		if(id != null) { 
			try{
				bestia = new Bestia(Integer.parseInt(id));
				bestia = controlador.getOne(bestia);
			}catch(NumberFormatException e) {
				logger.log(Level.WARNING, "Error al parsear id en el servlet ObtenerRegistroBestia", e);
				request.setAttribute("errorGlobal", "La id de la bestia es inválida. ");
				return;
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al conseguir bestia en el servlet ObtenerRegistroBestia", e);
				request.setAttribute("errorGlobal", "No se ha conseguido la bestia. ");
				return;
			}
		}
		String nroRegistro = request.getParameter("nroRegistro");
		Registro registro = null;
		if(id != null) {
			if(nroRegistro != null) {
				try{
					registro = new Registro(Integer.parseInt(nroRegistro), bestia);
					registro = controladorRegistro.getOne(registro);
				}catch(NumberFormatException e) {
					logger.log(Level.WARNING, "Error al parsear nroRegistro en el servlet ObtenerRegistroBestia", e);
					request.setAttribute("errorGlobal", "El número de registro es inválido. ");
					return;
				}catch(Exception e) {
					logger.log(Level.SEVERE, "Error al conseguir registro en el servlet ObtenerRegistroBestia", e);
					request.setAttribute("errorGlobal", "No se ha conseguido el registro. ");
					return;
				}
			}else {
				LocalDateTime fechaParseada;
				try{
					if(fecha == null) {
						fechaParseada = LocalDateTime.now();
					}else{
						fechaParseada = LocalDateTime.parse(fecha);
					}
					registro = controladorRegistro.getRegistroToShow(bestia, fechaParseada);
				}catch(DateTimeParseException e) {
					logger.log(Level.WARNING, "Error al parsear fecha en el servlet ObtenerRegistroBestia", e);
					request.setAttribute("errorGlobal", "La fecha es inválida. ");
					return;
				}catch(Exception e) {
					logger.log(Level.SEVERE, "Error al conseguir registro en el servlet ObtenerRegistroBestia", e);
					request.setAttribute("errorGlobal", "No se ha conseguido el registro. ");
					return;
				}
			}
		}

		try{	
			imagen = (CloudinaryHelper.getImagenRegistro(registro != null? registro.getMainPic() : EnvHelper.get("DEFAULT_PICTURE_ID")));
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir imagen en el servlet ObtenerRegistroBestia", e);
			request.setAttribute("errorGlobal", "No se ha conseguido la imagen del registro. ");
			return;
		}
		request.setAttribute("UrlImagen", imagen);
		request.setAttribute("foundBestia", bestia);
		request.setAttribute("foundRegistro", registro);
		rd.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
