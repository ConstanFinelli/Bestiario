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
import java.util.ArrayList;
import java.util.List;
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
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String peligrosidad = request.getParameter("peligrosidad");
		String estado = request.getParameter("estado");
		Bestia bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad, estado);
		String imagen = null;
		List<String> errores = new ArrayList<>();
		
		try {
			imagen = CloudinaryHelper.getImagenEditarBestia(controladorRegistro.getImagen(bestia, LocalDateTime.now()));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener la imagen de la bestia en el servlet ActualizarBestia", e);
			errores.add("No se ha podido obtener la imagen de la bestia");
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP(""));
		try {
			bestia = controlador.update(bestia);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al actualizar la bestia en el servlet ActualizarBestia", e);
			errores.add("No se ha podido actualizar la bestia");
		}
		
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde");
			request.setAttribute("errorGlobal", errores);
		}
		request.getSession().setAttribute("updateBestia", bestia);		
		request.getSession().setAttribute("imagen", imagen);
		rd.forward(request, response);
		
	}

	}

