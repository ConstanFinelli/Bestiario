package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicHabitat;

import java.io.IOException;
import java.util.LinkedList;

import entities.Bestia;
import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CambiarHabitat
 */
@WebServlet("/bestias/cambiarHabitat")
public class CambiarHabitat extends HttpServlet {
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private LogicBestia controlador = new LogicBestia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambiarHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP(""));
    	rd.forward(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id));
		bestia = controlador.getOne(bestia);
		String idHabitat = request.getParameter("idHabitat");
		Habitat ht = controladorHabitat.getOne(new Habitat(Integer.parseInt(idHabitat)));
		LinkedList<Habitat> habitatsBestia = bestia.getHabitats();
		boolean isIn = false;
		for(Habitat habitat:habitatsBestia) {
			if(habitat.getId() == ht.getId()) {
				isIn = true;
		}
		}
		if(!isIn) {
			habitatsBestia.add(ht);
			bestia.setHabitats(habitatsBestia);
			controlador.saveHabitats(bestia);
		}else {
			controlador.removeRelation(bestia, ht);
			bestia.getHabitats().remove(ht);
		}
		
		request.getSession().setAttribute("bestia", bestia);
		
		request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP("")).forward(request, response);
	}

}
