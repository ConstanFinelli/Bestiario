package servlet.auth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.DataPasswordResetToken;
import data.DataUsuario;
import entities.PasswordResetToken;
import entities.Usuario;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEmail;
/**
 * Servlet implementation class SvForgotPassword
 */
@WebServlet("/forgot-password")
public class SvForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SvForgotPassword.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvForgotPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                HttpRoutes.FORGOT_PASSWORD_JSP("")
        ).forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
String correo = request.getParameter("correo");
		
		DataUsuario daoUsuario = new DataUsuario();
		
		Usuario usuario = null;
		try {
			usuario = daoUsuario.getByEmail(correo);
		} catch(DataNotFoundException e) {
			logger.log(Level.WARNING, "Correo no encontrado en SvForgotPassword: " + correo, e);
			usuario = null;
		} catch(Exception e) {
			logger.log(Level.SEVERE, "Error al buscar usuario por email en SvForgotPassword", e);
			usuario = null;
		}
		
		if(usuario != null) {
			DataPasswordResetToken daoToken = new DataPasswordResetToken();
			String token = UUID.randomUUID().toString();
			
			PasswordResetToken resetToken = new PasswordResetToken();
			resetToken.setToken(token);
			resetToken.setIdUsuario(usuario.getIdUsuario());
			resetToken.setExpiration(LocalDateTime.now().plusMinutes(30));
			resetToken.setUsed(false);
			
			try {
				daoToken.deleteByUser(usuario.getIdUsuario());
			}catch(Exception e) {
				logger.log(Level.WARNING, "Error crítico al eliminar el token del usuario en el servlet SvForgotPassword", e);
			}
			try {
			daoToken.save(resetToken);
			}catch(Exception e) {
				logger.log(Level.WARNING, "Error crítico al agregar el token del usuario en el servlet SvForgotPassword", e);
			}
			int port = request.getServerPort();
			String scheme = request.getScheme();
			String host = request.getServerName();
			boolean isDefaultPort = ("http".equalsIgnoreCase(scheme) && port == 80) || ("https".equalsIgnoreCase(scheme) && port == 443);
			String baseUrl = scheme + "://" + host + (isDefaultPort ? "" : ":" + port);
			String link = baseUrl + HttpRoutes.RESET_PASSWORD(request.getContextPath()) + "?token=" + token;
			
			LogicEmail logicEmail = new LogicEmail();
			final String emailDestino = usuario.getCorreo();
			
			new Thread(() -> {
			    try {
			        logicEmail.notificarCambioContraseña(emailDestino, link);
			    } catch(Exception e) {
					logger.log(Level.WARNING, "Error crítico en el hilo de envío de mail en el servlet SvForgotPassword", e);
				}
			}).start();
			
			request.getSession().setAttribute("successMsg", "Mail de recuperción enviado a: " + correo);
			response.sendRedirect(HttpRoutes.LOGIN_JSP(request.getContextPath()));
		} else {
			
			request.getSession().setAttribute("logMsg", "El Correo ingresado no pertenece a nigún usuario");
			response.sendRedirect(HttpRoutes.FORGOT_PASSWORD_JSP(request.getContextPath()));
		}
		
	}

}
