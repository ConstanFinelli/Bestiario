package servlet.auth;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Lector;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class SvRegister
 */
@WebServlet("/auth/register")
public class SvRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public LogicUsuario logicUsuario = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(SvRegister.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("correo");
		String logMessage = "";
		List<String> errores = new ArrayList<>();
		Usuario user = null;
		try {
			user = logicUsuario.getByEmail(email);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir usuario en el servlet SvRegister", e);
			errores.add("No se ha podido conseguir el usuario");
		}
		String fecha = request.getParameter("fechaNacimiento");
		String password = request.getParameter("password");
		String flag = request.getParameter("flag");
		RequestDispatcher rd = null;
		LocalDate fechaSinHora = null;
		if(fecha != null) {
			fechaSinHora = LocalDate.parse(fecha);
		}
		if(user != null) {
			logMessage = "Ya existe un usuario registrado con ese email";
		} else {
				Lector userLector = new Lector(
					email,
					password,
					fechaSinHora.atStartOfDay()
				);
				
				userLector.setRecibirNotificaciones(Boolean.parseBoolean(request.getParameter("recibirNotiticaciones")));
				
				try {
					logicUsuario.save(userLector);
				}catch(Exception e) {
					logger.log(Level.SEVERE, "Error al registrar usuario en el servlet SvRegister", e);
					errores.add("No se ha podido registrar el usuario");
				}
			if(flag == null) {
				request.getSession().setAttribute("successMsg", "Usuario creado con éxito");
				response.sendRedirect(HttpRoutes.LOGIN_JSP(request.getContextPath()));	
				return;
			}
		}
		
		if("admin".equals(flag)) {
			rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=usuarios");
			request.setAttribute("feedbackMessage", "¡Usuario creado con éxito!");
		}else {
			rd = request.getRequestDispatcher(HttpRoutes.REGISTER_JSP(""));
		}
		
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde");
			request.setAttribute("errorGlobal", errores);
		}
		request.setAttribute("logMsg", logMessage);
		rd.forward(request, response);
	}

}
