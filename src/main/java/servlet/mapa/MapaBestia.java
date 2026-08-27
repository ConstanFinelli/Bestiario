package servlet.mapa;

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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Registro;
import helpers.EnvHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class MapaBestia
 */
@WebServlet("/mapas/bestia")
public class MapaBestia extends HttpServlet {
	LogicBestia controladorBestia = new LogicBestia();
	LogicRegistro controladorRegistro = new LogicRegistro();
	private static final Logger logger = Logger.getLogger(MapaBestia.class.getName());

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapaBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.MAPA_JSP(""));
		String idBestia = request.getParameter("id");
		Bestia bestia = null;
		try{
			bestia = new Bestia(Integer.parseInt(idBestia));
			bestia = controladorBestia.getOne(bestia);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error al parsear el id de la bestia en el servlet MapaBestia", nfe);
			request.setAttribute("errorGlobal","Id de bestia invalido");
			rd.forward(request, response);
			return;
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener la bestia en el servlet MapaBestia", e);
			request.setAttribute("errorGlobal","No se ha podido obtener la bestia");
			rd.forward(request, response);
			return;
		}

		Registro registro = null;

		try{
			registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener el registro de la bestia en el servlet MapaBestia", e);
			request.setAttribute("errorGlobal","No se ha podido obtener el registro de la bestia");
		}
		
		if(bestia.getHabitats().isEmpty()) {
			request.setAttribute("errorGlobal","La bestia no tiene habitats asociados");
		}

		Map<Bestia, String> bestias = new HashMap<Bestia, String>();
		bestias.put(bestia, registro != null? registro.getMainPic(): EnvHelper.get("DEFAULT_PICTURE_ID"));
		request.getSession().setAttribute("bestias", bestias);
		request.setAttribute("selectedBestia", bestia);
	
		rd.forward(request, response);
	}

}
