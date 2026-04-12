package servlet.bestia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

import java.io.IOException;

import entities.Bestia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarBestia
 */
@WebServlet("/bestias/eliminar")
public class EliminarBestia extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id));
		bestia = controlador.delete(bestia);
		controladorRegistro.deleteImages(bestia);
		request.setAttribute("deletedBestia", bestia);
		response.sendRedirect(HttpRoutes.LISTAR_BESTIAS(request.getContextPath()));		
		
	}

}
