package servlet.Investigador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;

import entities.Investigador;
import helpers.HttpRoutes;

/**
 * Servlet implementation class RechazarSolicitud
 */
@WebServlet("/investigadores/rechazarSolicitud")
public class RechazarSolicitud extends HttpServlet {
	private LogicUsuario controladorUsuario = new LogicUsuario();
	
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
		try {
			Investigador user = new Investigador(Integer.parseInt(idUsuario));
			user = (Investigador) controladorUsuario.getOne(user);	
			user.setDni(null);
			user.setApellido(null);
			user.setNombre(null);
			user.setEstado("lector");
			controladorUsuario.update(user);
		} catch(NumberFormatException e) {
			e.getMessage();
		}
		response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
	}

}
