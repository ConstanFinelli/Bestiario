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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import entities.Bestia;
import entities.Registro;
import helpers.CloudinaryHelper;
import helpers.EnvHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class Listar
 */
@WebServlet("/bestias/listar")
public class ListarBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicBestia controladorBestia = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	
    public ListarBestia() {
        super();
   
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.BESTIA_LIST_JSP(""));
		String filter = request.getParameter("filter");
		LinkedList<Bestia> bestias = new LinkedList<>();
		Map<Bestia, String> imagenes = new HashMap<Bestia, String>();
		if(filter != null && !filter.isEmpty()) {
			bestias = controladorBestia.findByCategoria(filter);
		}else {
			bestias = controladorBestia.findAll();
		}
		if (!bestias.isEmpty()) {
			for(Bestia bestia: bestias) {
				Registro registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
				imagenes.put(bestia, CloudinaryHelper.getImagenListadoBestia(registro != null? registro.getMainPic() : EnvHelper.get("DEFAULT_PICTURE_ID")));
			}
			request.setAttribute("bestias", bestias);
			request.setAttribute("imagenes", imagenes);
		}
		if(filter != null) {
			request.setAttribute("searchedFilter", filter); 
		}
		rd.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request, response);
	}
	


}
