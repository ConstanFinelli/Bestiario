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
import java.util.LinkedList;

import entities.Bestia;
import entities.Registro;
import helpers.CloudinaryHelper;
import helpers.ConstantesHelper;
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
		if(filter != null && !filter.isEmpty()) {
			bestias = controladorBestia.findByCategoria(filter);
		}else {
			bestias = controladorBestia.findAll();
		}
		if (!bestias.isEmpty()) {
			for(Bestia bestia: bestias) {
				Registro registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
				bestia.setPictureUrl(CloudinaryHelper.getImagenListadoBestia(registro != null? registro.getMainPic() : ConstantesHelper.DEFAULT_IMAGE));
			}
			request.setAttribute("bestias", bestias);
		}
		if(filter != null) {
			request.setAttribute("searchedFilter", filter); 
		}
		rd.forward(request, response);
	}

	


}
