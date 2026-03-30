package servlet.lector;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import entities.Lector;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarCategoria
 */
@WebServlet("/lectores/actualizar")
public class ActualizarLector extends HttpServlet {
	private LogicUsuario controlador = new LogicUsuario();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarLector() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=usuarios");
		String id = request.getParameter("id");
		String email = request.getParameter("email");
		String fecha = request.getParameter("fechaNacimiento");
		String password = request.getParameter("password");
		String feedbackMessage = "";
		LocalDate fechaSinHora = null;
		if(fecha != null) {
			fechaSinHora = LocalDate.parse(fecha);
		}
		Lector lector = new Lector(Integer.parseInt(id),email, password, fechaSinHora.atStartOfDay(), "lector");
		lector = (Lector) controlador.update(lector);
		if(lector == null) {
			feedbackMessage = "¡No se ha podido actualizar el usuario!";
		}else {
			feedbackMessage = "¡Usuario actualizada con éxito!";
		}
		request.setAttribute("feedbackMessage", feedbackMessage);
		rd.forward(request, response);
	}

}
