package servlet.Investigador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;

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
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearSolicitud() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idUsuario = request.getParameter("idUsuario");
		String nombre = request.getParameter("nombre");
		String apellido = request.getParameter("apellido");
		String dni = request.getParameter("dni");
		Lector user = (Lector) controladorUsuario.getOne(new Usuario(Integer.parseInt(idUsuario)));
		
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.PRESENTAR_CANDIDATURA_JSP(""));
		
		Investigador solicitud = new Investigador (user.getIdUsuario(), user.getCorreo(), user.getContraseña(), nombre, apellido, dni, "solicitante");
		if(controladorUsuario.update(solicitud) != null) {
			response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
		}else {
			request.setAttribute("errorMsg", "Ya hay un investigador registrado con este DNI.");
			rd.forward(request, response);
		}
	}

}
