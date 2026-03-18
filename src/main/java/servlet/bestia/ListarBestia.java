package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;

import java.io.IOException;
import java.util.LinkedList;

import entities.Bestia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class Listar
 */
@WebServlet("/bestias/listar")
public class ListarBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicBestia controladorBestia = new LogicBestia();
	
    public ListarBestia() {
        super();
   
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.BESTIA_LIST_JSP(""));
		String filter = request.getParameter("filter");
		LinkedList<Bestia> bestias = new LinkedList<>();
		if(filter != null && !filter.isEmpty()) {
			bestias = controladorBestia.findByCategoria(filter);
		}else {
			bestias = controladorBestia.findAll();
		}
		if (!bestias.isEmpty()) {
			request.setAttribute("bestias", bestias);
		}
		if(filter != null) {
			request.setAttribute("searchedFilter", filter); 
		}
		rd.forward(request, response);
	}

	


}
