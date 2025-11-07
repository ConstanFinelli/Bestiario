package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import entities.Investigador;
import entities.Lector;
import entities.Usuario;

/**
 * Servlet implementation class SvRegister
 */
@WebServlet("/SvRegister")
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
		LocalDate fechaSinHora = null;
		if(fecha != null) {
			fechaSinHora = LocalDate.parse(fecha);
		}
		if(user != null) {
			logMessage = "Ya existe un usario registrado con ese email";
		} else { 
				Lector userLector = new Lector(
					email,
					request.getParameter("contrasena"),
					fechaSinHora.atStartOfDay()
				);
				logicUsuario.save(userLector);
			response.sendRedirect("login.jsp");	
			return;
		}
		
		request.setAttribute("logMsg", logMessage);
		request.getRequestDispatcher("register.jsp").forward(request, response);
	}

}
