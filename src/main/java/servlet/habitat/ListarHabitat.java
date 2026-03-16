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

import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarHabitat
 */
@WebServlet("/habitats/listar")
public class ListarHabitat extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicHabitat controlador = new LogicHabitat();
       
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
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.HABITATS_JSP(""));
		LinkedList<Habitat> hts = new LinkedList<>();
		hts = controlador.findAll();
		request.setAttribute("habitats", hts);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
