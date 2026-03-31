package servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import data.DataPasswordResetToken;
import data.DataUsuario;
import entities.PasswordResetToken;
import logic.LogicUsuario;

/**
 * Servlet implementation class SvResetPassword
 */
@WebServlet("/reset-password")
public class SvResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvResetPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter("token");
		DataPasswordResetToken tokenDao = new DataPasswordResetToken();
		
		PasswordResetToken t = tokenDao.getOne(token);

		if (t == null || t.isUsed() || t.getExpiration().isBefore(LocalDateTime.now())) {
		    response.getWriter().println("Token inválido o expirado");
		    return;
		}

		// válido → mostrar form
		request.setAttribute("token", token);
		request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter("token");
		String nuevaPassword = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		
		if(confirmPassword != nuevaPassword) {
			request.setAttribute("logMsg", "Las contraseñas ingresadas no coinciden");
			request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
		}
		
		DataPasswordResetToken tokenDao = new DataPasswordResetToken();
		DataUsuario usuarioDao = new DataUsuario();

		PasswordResetToken t = tokenDao.getOne(token);

		if (t == null || t.isUsed() || t.getExpiration().isBefore(LocalDateTime.now())) {
		    response.getWriter().println("Token inválido");
		    return;
		}

		// cambiar contraseña
		usuarioDao.updatePassword(t.getIdUsuario(), LogicUsuario.hashPassword(nuevaPassword));

		// invalidar token
		tokenDao.markAsUsed(token);
		
		request.setAttribute("msg", "Contraseña actualizada correctamente");
		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

}
