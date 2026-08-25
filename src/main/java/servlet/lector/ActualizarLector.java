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
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Lector;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarCategoria
 */
@WebServlet("/lectores/actualizar")
public class ActualizarLector extends HttpServlet {
	private LogicUsuario controlador = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(ActualizarLector.class.getName());
	
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
		LocalDate fechaSinHora = null;
		if(fecha != null) {
			try{
				fechaSinHora = LocalDate.parse(fecha);
			}
			catch(Exception e) {
				logger.log(Level.WARNING, "Error al parsear fecha en el servlet ActualizarLector", e);
				request.setAttribute("errorGlobal", "La fecha ingresada no es válida. ");
				rd.forward(request, response);
				return;
			}
		}

		Lector lector = null;
		try{
			lector = new Lector(Integer.parseInt(id),email, password, fechaSinHora.atStartOfDay(), "lector");
		}
		catch(Exception e) {
			logger.log(Level.WARNING, "Error al crear lector en el servlet ActualizarLector", e);
			request.setAttribute("errorGlobal", "No se ha podido crear el lector. ");
			rd.forward(request, response);
			return;
		}
		try{
			lector = (Lector) controlador.getOne(lector);
		}
		catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir lector en el servlet ActualizarLector", e);
			request.setAttribute("errorGlobal", "No se ha podido conseguir el lector. ");
			rd.forward(request, response);
			return;
		}
		lector = (Lector) controlador.update(lector);
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage","¡Usuario actualizado con éxito!");
		}
		rd.forward(request, response);
	}

}
