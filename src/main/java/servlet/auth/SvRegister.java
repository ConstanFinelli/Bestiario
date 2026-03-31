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
		Usuario user = logicUsuario.getByEmail(email);
		String fecha = request.getParameter("fechaNacimiento");
		String password = request.getParameter("password");
		String flag = request.getParameter("flag");
		RequestDispatcher rd = null;
		LocalDate fechaSinHora = null;
		if(fecha != null) {
			fechaSinHora = LocalDate.parse(fecha);
		}
		if(user != null) {
			logMessage = "Ya existe un usario registrado con ese email";
		} else {
				Lector userLector = new Lector(
					email,
					password,
					fechaSinHora.atStartOfDay()
				);
				
				userLector.setRecibirNotificaciones(Boolean.parseBoolean(request.getParameter("recibirNotiticaciones")));
				
				logicUsuario.save(userLector);
			if(flag == null) {
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
		
		request.setAttribute("logMsg", logMessage);
		rd.forward(request, response);
	}

}
