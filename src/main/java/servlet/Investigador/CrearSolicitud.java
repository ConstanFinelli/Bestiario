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

import entities.Investigador;
import entities.Lector;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CrearSolicitud
 */
@WebServlet("/investigadores/crearSolicitud")
public class CrearSolicitud extends HttpServlet {
	private LogicUsuario controladorUsuario = new LogicUsuario();
	
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
		System.out.println(user.getContraseña());
		String nombre = request.getParameter("nombre");
		String apellido = request.getParameter("apellido");
		String dni = request.getParameter("dni");
		
		Investigador solicitud = new Investigador (user.getIdUsuario(), user.getCorreo(), LogicUsuario.dehashPassword(user.getContraseña()), nombre, apellido, dni, "solicitante");
		if(controladorUsuario.update(solicitud) != null) {
			user.setEstado("solicitante");
			response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
		}
	}

}
