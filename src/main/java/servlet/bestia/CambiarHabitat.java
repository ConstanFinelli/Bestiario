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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private static final Logger logger = Logger.getLogger(CambiarHabitat.class.getName());
	
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
		List<String> errores = new ArrayList<>();
		Bestia bestia = new Bestia(Integer.parseInt(id));
		Habitat ht = null;
		String idHabitat = request.getParameter("idHabitat");
		try {
			bestia = controlador.getOne(bestia);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener la bestia en el servlet CambiarHabitat", e);
			errores.add("No se ha podido obtener la bestia seleccionada");
		}
		
		try {
			ht = controladorHabitat.getOne(new Habitat(Integer.parseInt(idHabitat)));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener habitat en el servlet CambiarHabitat", e);
			errores.add("No se ha podido obtener habitat de la bestia seleccionada");
		}
		LinkedList<Habitat> habitatsBestia = bestia.getHabitats();
		boolean isIn = false;
		if(habitatsBestia != null && ht != null) {
			for(Habitat habitat:habitatsBestia) {
				if(habitat.getId() == ht.getId()) {
					isIn = true;
				}
			}
			if(!isIn) {
				habitatsBestia.add(ht);
				bestia.setHabitats(habitatsBestia);
				try {
					controlador.saveHabitats(bestia);
				}catch(Exception e) {
					logger.log(Level.WARNING, "Error al guardar habitat en la bestia en el servlet CambiarHabitat", e);
					errores.add("No se ha podido guardar el habitat en la bestia seleccionada");
				}
			}else {
				try {
					controlador.removeRelation(bestia, ht);
				}catch(Exception e) {
					logger.log(Level.WARNING, "Error al remover habitat en la bestia en el servlet CambiarHabitat", e);
					errores.add("No se ha podido remover el habitat en la bestia seleccionada");
				}
				bestia.getHabitats().remove(ht);
			}
		}
		
		if(!errores.isEmpty()) {
			errores.add("");
			request.setAttribute("errorGlobal", errores);
		}
		
		request.getSession().setAttribute("bestia", bestia);
		
		request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP("")).forward(request, response);
	}

}
