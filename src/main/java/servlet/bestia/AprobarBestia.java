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
		Bestia bestia = null;
		try {
			bestia = new Bestia(Integer.parseInt(bestiaId));
			controladorBestia.approve(bestia);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error al parsea el id de la bestia en el servlet AprobarBestia", nfe);
			request.setAttribute("errorGlobal","Id de bestia invalido");
			request.getRequestDispatcher(HttpRoutes.LISTAR_BESTIAS("")).forward(request, response);
			return;
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al aprobar la bestia en el servlet AprobarBestia", e);
			request.setAttribute("errorGlobal","No se ha podido aprobar la bestia");
			request.getRequestDispatcher(HttpRoutes.LISTAR_BESTIAS("")).forward(request, response);
			return;
		}
		response.sendRedirect(HttpRoutes.LISTAR_BESTIAS(request.getContextPath()));
	}

}
