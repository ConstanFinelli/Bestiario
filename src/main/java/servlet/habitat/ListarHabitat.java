package servlet.habitat;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicHabitat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarHabitat
 */
@WebServlet("/habitats/listar")
public class ListarHabitat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicHabitat controlador = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(ListarHabitat.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = null;
		String flag = request.getParameter("flag");
		LinkedList<Habitat> hts = new LinkedList<>();
		try{
			hts = controlador.findAll();
		}
		catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir habitats en el servlet ListarHabitat", e);
			request.setAttribute("errorGlobal", "No se ha podido conseguir los habitats. ");
		}
		request.setAttribute("habitats", hts);
		
		if(flag == null) {
			rd = request.getRequestDispatcher(HttpRoutes.HABITATS_JSP(""));
		}else {
			rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats");
		}
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
