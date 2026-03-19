package servlet.auth;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicUsuario;

import java.io.IOException;

import entities.Usuario;
import helpers.HttpRoutes;

@WebServlet("/auth/login")
public class SvLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LogicUsuario controladorUsuario = new LogicUsuario();   
	
    public SvLogin() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.LOGIN_JSP(""));
    	rd.forward(request, response);
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.LOGIN_JSP(""));
		String correo = request.getParameter("correo");
		String contrasena = request.getParameter("contrasena");
		String logMsg = "";
		
		
		Usuario usuario = controladorUsuario.getByEmail(correo);
		
		if(usuario != null) {
			if(contrasena.equals(LogicUsuario.dehashPassword(usuario.getContraseña()))) {
				HttpSession session = request.getSession();
	            session.setAttribute("user", usuario);
	            response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
	            return;
			}else {
				logMsg = "Contraseña incorrecta";
			}
		}else {
			logMsg = "Usuario no encontrado";
		}
		request.setAttribute("logMsg", logMsg);
		rd.forward(request, response);
	}

}
