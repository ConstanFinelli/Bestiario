package servlet.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import data.DataPasswordResetToken;
import data.DataUsuario;
import entities.PasswordResetToken;
import entities.Usuario;
import logic.LogicEmail;
/**
 * Servlet implementation class SvForgotPassword
 */
@WebServlet("/forgot-password")
public class SvForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String correo = request.getParameter("correo");
		
		DataUsuario daoUsuario = new DataUsuario();
		
		Usuario usuario = daoUsuario.getByEmail(correo);
		
		if(usuario != null) {
			DataPasswordResetToken daoToken = new DataPasswordResetToken();
			String token = UUID.randomUUID().toString();
			
			PasswordResetToken resetToken = new PasswordResetToken();
			resetToken.setToken(token);
			resetToken.setIdUsuario(usuario.getIdUsuario());
			resetToken.setExpiration(LocalDateTime.now().plusMinutes(30));
			resetToken.setUsed(false);

			daoToken.save(resetToken);
			
			String link = "http://localhost:8080/Bestiario/reset-password?token=" + token;
			
			LogicEmail logicEmail = new LogicEmail();
			
			logicEmail.enviarEmail(
			    usuario.getCorreo(),
			    "Recuperar contraseña",
			    "Click acá para cambiar tu contraseña:\n" + link
			);
		}
		
	}

}
