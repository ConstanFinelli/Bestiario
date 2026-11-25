package servlet.auth;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Lector;
import entities.Usuario;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicUsuario;

/**
 * Servlet implementation class SvRegister
 */
@WebServlet("/auth/register")
public class SvRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public LogicUsuario logicUsuario = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(SvRegister.class.getName());

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(HttpRoutes.REGISTER_JSP("")).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("correo");
		String fecha = request.getParameter("fechaNacimiento");
		String password = request.getParameter("password");
		String flag = request.getParameter("flag");
		HttpSession session = request.getSession(false);
		Usuario currentUser = (session != null) ? (Usuario) session.getAttribute("user") : null;
		boolean isAdminFlow = "admin".equals(flag);
		if (isAdminFlow && (currentUser == null || !"investigador".equalsIgnoreCase(currentUser.getEstado()))) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: se requieren permisos de investigador.");
			return;
		}
		String logMessage = "";
		List<String> errores = new ArrayList<>();
		RequestDispatcher rd = null;

		LocalDate fechaSinHora = null;
		if (email == null || email.isBlank() || password == null || password.isBlank() || fecha == null || fecha.isBlank()) {
			logMessage = "Todos los campos obligatorios deben estar completos.";
		} else {
			try {
				fechaSinHora = LocalDate.parse(fecha.trim());
			} catch (DateTimeParseException e) {
				logger.log(Level.WARNING, "Fecha de nacimiento inválida en SvRegister: " + fecha, e);
				logMessage = "La fecha de nacimiento ingresada no tiene un formato válido.";
			}
		}

		if (logMessage.isEmpty()) {
			Usuario user = null;
			try {
				user = logicUsuario.getByEmail(email.trim());
			} catch (DataNotFoundException e) {
				user = null;
			} catch (Exception e) {
				logger.log(Level.WARNING, "Error al conseguir usuario en el servlet SvRegister", e);
				errores.add("No se ha podido conseguir el usuario");
			}

			if (user != null) {
				logMessage = "Ya existe un usuario registrado con ese email";
			} else if (errores.isEmpty()) {
				Lector userLector = new Lector(
					email.trim(),
					password,
					fechaSinHora.atStartOfDay()
				);
				String recibirNotificaciones = request.getParameter("recibirNotificaciones");
				userLector.setRecibirNotificaciones(recibirNotificaciones != null);

				try {
					logicUsuario.save(userLector);
					if (!isAdminFlow) {
						request.getSession().setAttribute("successMsg", "Usuario creado con éxito");
						response.sendRedirect(HttpRoutes.LOGIN_JSP(request.getContextPath()));
						return;
					}
					request.setAttribute("feedbackMessage", "¡Usuario creado con éxito!");
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Error al registrar usuario en el servlet SvRegister", e);
					errores.add("No se ha podido registrar el usuario");
				}
			}
		}

		if (isAdminFlow) {
			rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=usuarios");
		} else {
			rd = request.getRequestDispatcher(HttpRoutes.REGISTER_JSP(""));
		}

		if (!errores.isEmpty()) {
			request.setAttribute("errorGlobal", String.join("<br>", errores));
		}
		request.setAttribute("logMsg", logMessage);
		rd.forward(request, response);
	}

}
