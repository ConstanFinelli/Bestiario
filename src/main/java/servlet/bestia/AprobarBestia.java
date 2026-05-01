package servlet.bestia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;

import java.io.IOException;

import entities.Bestia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class AprobarBestia
 */
@WebServlet("/bestias/aprobar")
public class AprobarBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicBestia controladorBestia = new LogicBestia();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AprobarBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bestiaId = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(bestiaId));
		controladorBestia.approve(bestia);
		response.sendRedirect(HttpRoutes.LISTAR_BESTIAS(request.getContextPath()));
	}

}
