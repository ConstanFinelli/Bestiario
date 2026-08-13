package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarBestia
 */
@WebServlet("/bestias/actualizar")
public class ActualizarBestia extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private static final Logger logger = Logger.getLogger(ActualizarBestia.class.getName());

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP(""));
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String peligrosidad = request.getParameter("peligrosidad");
		String estado = request.getParameter("estado");
		Bestia bestia = null;
		try {
			bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad, estado);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error parseando la id de la bestia en el servlet ActualizarBestia");
			request.setAttribute("errorGlobal", "Id invalida");
			rd.forward(request, response);
			return;
		}
		String imagen = null;
		
		try {
			imagen = CloudinaryHelper.getImagenEditarBestia(controladorRegistro.getImagen(bestia, LocalDateTime.now()));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener la imagen de la bestia en el servlet ActualizarBestia", e);
			request.setAttribute("errorGlobal", "No se ha podido obtener la imagen de la bestia");
			rd.forward(request, response);
			return;
		}
		try {
			bestia = controlador.update(bestia);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al actualizar la bestia en el servlet ActualizarBestia", e);
			request.setAttribute("errorGlobal", "No se ha podido actualizar la bestia");
			rd.forward(request, response);
			return;
		}
		
		request.getSession().setAttribute("updateBestia", bestia);		
		request.getSession().setAttribute("imagen", imagen);
		rd.forward(request, response);
		
	}

	}

