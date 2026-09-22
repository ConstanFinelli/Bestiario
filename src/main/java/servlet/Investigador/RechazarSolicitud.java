package servlet.Investigador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Investigador;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;

/**
 * Servlet implementation class RechazarSolicitud
 */
@WebServlet("/investigadores/rechazarSolicitud")
public class RechazarSolicitud extends HttpServlet {
	private LogicUsuario controladorUsuario = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(RechazarSolicitud.class.getName());

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idUsuario = request.getParameter("idUsuario");
		Investigador user = null;
		try {
			user = new Investigador(Integer.parseInt(idUsuario));
			user = (Investigador) controladorUsuario.getOne(user);	
			user.setDni(null);
			user.setApellido(null);
			user.setNombre(null);
			user.setEstado("lector");
		} catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear idUsuario en el servlet RechazarSolicitud", e);
			request.setAttribute("errorGlobal", "La id del usuario es inválida. ");
			response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
			return;
		} catch(DataNotFoundException e) {
			logger.log(Level.WARNING, "Usuario no encontrado en el servlet RechazarSolicitud", e);
			request.setAttribute("errorGlobal", "El usuario no fue encontrado. ");
			response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
			return;
		} catch(Exception e) {
			logger.log(Level.SEVERE, "Error al rechazar solicitud en el servlet RechazarSolicitud", e);
			request.setAttribute("errorGlobal", "No se ha conseguido el usuario. ");
			response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
			return;
		}

		try{
			controladorUsuario.update(user);
		} catch(DataNotFoundException e) {
			logger.log(Level.WARNING, "Usuario no encontrado al actualizar en el servlet RechazarSolicitud", e);
			request.setAttribute("errorGlobal", "El usuario no existe. ");
			response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
			return;
		} catch(Exception e) {
			logger.log(Level.SEVERE, "Error al rechazar solicitud en el servlet RechazarSolicitud", e);
			request.setAttribute("errorGlobal", "No se ha podido rechazar la solicitud. ");
			response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
			return;
		}
		response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
	}

}
