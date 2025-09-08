package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;
import java.time.LocalDate;

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
		String email = request.getParameter("email");
		String logMessage = "";
		Usuario user = logicUsuario.getByEmail(email);
		
		if(user != null) {
			logMessage = "Ya existe un usario registrado con ese email";
		} else { 
			user.setContraseña(request.getParameter("password"));
			user.setCorreo(email);
			user.setEsInvestigador(Boolean.parseBoolean(request.getParameter("isInvestigador")));
			if(user.isEsInvestigador()) {
				Investigador userInvestigador = new Investigador(
					user.getCorreo(),
					user.getContraseña(),
					request.getParameter("nombre"),
					request.getParameter("apellido"),
					request.getParameter("dni")
				);
				logicUsuario.save(userInvestigador);
			} else {
				Lector userLector = new Lector(
					user.getCorreo(),
					user.getContraseña(),
					LocalDate.parse(request.getParameter("fechaNacimiento"))
				);
				logicUsuario.save(userLector);
			}
			response.sendRedirect("login.jsp");	
			return;
		}
		
		request.setAttribute("logMsg", logMessage);
		request.getRequestDispatcher("register.jsp").forward(request, response);
	}

}
