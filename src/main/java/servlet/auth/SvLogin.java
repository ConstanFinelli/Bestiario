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
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Usuario;
import helpers.HttpRoutes;

@WebServlet("/auth/login")
public class SvLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public LogicUsuario controladorUsuario = new LogicUsuario();
    private static final Logger logger = Logger.getLogger(SvLogin.class.getName());
	
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
		Usuario usuario = null;
		
		
		try {
			usuario = controladorUsuario.getByEmail(correo);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir usuario en el servlet SvLogin", e);
			request.setAttribute("errorGlobal", "No se ha podido conseguir el usuario. ");
		}
		
		if(usuario != null) {
			if(contrasena.equals(LogicUsuario.dehashPassword(usuario.getContraseña()))) {
				HttpSession session = request.getSession();
	            session.setAttribute("user", usuario);
	            response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
	            return;
			}else {
				logMsg = "La contraseña ingresada es incorrecta.";
			}
		}else {
			logMsg = "El correo ingresado no está registrado.";
		}
		request.getSession().setAttribute("logMsg", logMsg);
		rd.forward(request, response);
	}

}
