package servlet.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import entities.Investigador;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class admin
 */
@WebServlet("/admin")
public class admin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public admin() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("user");
		if(usuario != null && usuario.getEstado().equals("investigador")) {
			response.sendRedirect(HttpRoutes.ADMIN_JSP(request.getContextPath()));
			return;
		}
		response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
	}


}
