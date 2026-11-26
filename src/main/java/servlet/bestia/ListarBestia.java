package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class Listar
 */
@WebServlet("/bestias/listar")
public class ListarBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicBestia controladorBestia = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private static final Logger logger = Logger.getLogger(ListarBestia.class.getName());
	
    public ListarBestia() {
        super();
   
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.BESTIA_LIST_JSP(""));
		String categoriaFilter = request.getParameter("categoriaFilter");
		String habitatFilter = request.getParameter("habitatFilter");
		LinkedList<Bestia> bestias = new LinkedList<>();
		Map<Bestia, String> imagenes = new LinkedHashMap<Bestia, String>();
		
		try {
			bestias = controladorBestia.findAll();
			if(categoriaFilter != null && !categoriaFilter.isEmpty()) {
				bestias = controladorBestia.filterByCategoria(bestias, categoriaFilter);
			}
			if(habitatFilter != null && !habitatFilter.isEmpty()) {
				bestias = controladorBestia.filterByHabitat(bestias, habitatFilter);
			}
			if (!bestias.isEmpty()) {
				bestias.sort((b1, b2) -> {
					if (b1.getNombre() == null) return -1;
					if (b2.getNombre() == null) return 1;
					return b1.getNombre().compareToIgnoreCase(b2.getNombre());
				});
				for(Bestia bestia: bestias) {
					imagenes.put(bestia, CloudinaryHelper.getImagenListadoBestia(controladorRegistro.getImagen(bestia, LocalDateTime.now())));
				}
				request.setAttribute("bestias", bestias);
				request.setAttribute("imagenes", imagenes);
			}
			if(categoriaFilter != null) {
				request.setAttribute("searchedCategoria", categoriaFilter); 
			}
			if(habitatFilter != null) {
				request.setAttribute("searchedHabitat", habitatFilter); 
			}
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error crítico al listar bestias en el servlet ListarBestia", e);
			request.setAttribute("errorGlobal", "No se han podido recuperar las bestias. ");
		}
		rd.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
	


}
