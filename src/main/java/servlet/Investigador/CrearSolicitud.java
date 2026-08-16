package servlet.Investigador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicUsuario;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Investigador;
import entities.Lector;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CrearSolicitud
 */
@WebServlet("/investigadores/crearSolicitud")
public class CrearSolicitud extends HttpServlet {
	private LogicUsuario controladorUsuario = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(CrearSolicitud.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearSolicitud() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sesion = request.getSession();
		Lector user = (Lector) sesion.getAttribute("user");
		String nombre = request.getParameter("nombre");
		String apellido = request.getParameter("apellido");
		String dni = request.getParameter("dni");
		
		if(user == null){
			logger.log(Level.WARNING, "Usuario no autenticado intentando crear solicitud");
			response.sendRedirect(HttpRoutes.LOGIN_JSP(request.getContextPath()));
			return;
		}

		
		Investigador solicitud = new Investigador (user.getIdUsuario(), user.getCorreo(), LogicUsuario.dehashPassword(user.getContraseña()), nombre, apellido, dni, "solicitante", user.getRecibirNotificaciones());
	
		Usuario actualizacion = null;
		try{
			actualizacion = controladorUsuario.update(solicitud);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error al crear solicitud de investigador en el servlet CrearSolicitud", e);
			request.setAttribute("errorGlobal", "No se ha podido crear la solicitud. ");
			return;
		}
		

		if(actualizacion != null) {

		if(controladorUsuario.update(solicitud) != null) {
			user.setEstado("solicitante");
			response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
		}
	}

}
