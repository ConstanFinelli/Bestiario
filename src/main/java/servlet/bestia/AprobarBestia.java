package servlet.bestia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class AprobarBestia
 */
@WebServlet("/bestias/aprobar")
public class AprobarBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicBestia controladorBestia = new LogicBestia();
	private static final Logger logger = Logger.getLogger(AprobarBestia.class.getName());

       
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
		try {
			controladorBestia.approve(bestia);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al guardar categoria en la bestia en el servlet CambiarCategoria", e);
			request.setAttribute("errorGlobal","No se ha podido guardar la categoria en la bestia seleccionada");
		}
		response.sendRedirect(HttpRoutes.LISTAR_BESTIAS(request.getContextPath()));
	}

}
