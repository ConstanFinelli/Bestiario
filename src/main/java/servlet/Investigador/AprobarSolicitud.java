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
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class AprobarSolicitud
 */
@WebServlet("/investigadores/aprobarSolicitud")
public class AprobarSolicitud extends HttpServlet {
	private LogicUsuario controladorUsuario = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(AprobarSolicitud.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AprobarSolicitud() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idUsuario = request.getParameter("idUsuario");
		Usuario user = null;
		try {
			user = new Usuario(Integer.parseInt(idUsuario));
			user = (Investigador) controladorUsuario.getOne(user);	
			user.setEstado("investigador");
		} catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear idUsuario en el servlet AprobarSolicitud", e);
			request.setAttribute("errorGlobal", "La id del usuario es inválida. ");
			return;
		} catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir usuario en el servlet AprobarSolicitud", e);
			request.setAttribute("errorGlobal", "No se ha conseguido el usuario. ");
			return;
		}

		try{
			controladorUsuario.update((Investigador) user);
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al aprobar solicitud en el servlet AprobarSolicitud", e);
			request.setAttribute("errorGlobal", "No se ha podido aprobar la solicitud. ");
		}

		response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
	}

}
