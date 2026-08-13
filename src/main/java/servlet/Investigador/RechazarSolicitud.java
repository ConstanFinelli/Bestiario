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
import helpers.HttpRoutes;

/**
 * Servlet implementation class RechazarSolicitud
 */
@WebServlet("/investigadores/rechazarSolicitud")
public class RechazarSolicitud extends HttpServlet {
	private LogicUsuario controladorUsuario = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(RechazarSolicitud.class.getName());

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RechazarSolicitud() {
        super();
        // TODO Auto-generated constructor stub
    }

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
			return;
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al rechazar solicitud en el servlet RechazarSolicitud", e);
			request.setAttribute("errorGlobal", "No se ha conseguido el usuario. ");
			return;
		}

		try{
			controladorUsuario.update(user);
		} catch(Exception e) {
			logger.log(Level.SEVERE, "Error al rechazar solicitud en el servlet RechazarSolicitud", e);
			request.setAttribute("errorGlobal", "No se ha podido rechazar la solicitud. ");
			return;
		}
		response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
	}

}
