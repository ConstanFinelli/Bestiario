package servlet.Investigador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;

import entities.Investigador;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class AprobarSolicitud
 */
@WebServlet("/investigadores/aprobarSolicitud")
public class AprobarSolicitud extends HttpServlet {
	private LogicUsuario controladorUsuario = new LogicUsuario();
	
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
		try {
			Usuario user = new Usuario(Integer.parseInt(idUsuario));
			user = (Investigador) controladorUsuario.getOne(user);	
			user.setEstado("investigador");
			controladorUsuario.update((Investigador) user);
		} catch(NumberFormatException e) {
			e.getMessage();
		}
		response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
	}

}
