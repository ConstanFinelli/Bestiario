package servlet.mapa;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Registro;
import exceptions.DataNotFoundException;
import helpers.EnvHelper;
import helpers.HttpRoutes;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

/**
 * Servlet implementation class MapaBestias
 */
@WebServlet("/mapas/bestias")
public class MapaBestias extends HttpServlet {
	LogicBestia controladorBestia = new LogicBestia();
	LogicRegistro controladorRegistro = new LogicRegistro();
	private static final Logger logger = Logger.getLogger(MapaBestias.class.getName());
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapaBestias() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.MAPA_JSP(""));
		String selectedId = request.getParameter("selectedId");
		if(selectedId != null) {
			Bestia bestia = null;
			try{
				bestia = new Bestia(Integer.parseInt(selectedId));
				bestia = controladorBestia.getOne(bestia);
			}catch(NumberFormatException nfe) {
				logger.log(Level.WARNING, "Error al parsear el id de la bestia seleccionada en el servlet MapaBestias", nfe);
				request.setAttribute("errorGlobal","Id de bestia inválido");
				rd.forward(request, response);
				return;
			}catch(DataNotFoundException dnfe) {
				logger.log(Level.WARNING, "Bestia seleccionada no encontrada en el servlet MapaBestias", dnfe);
				request.setAttribute("errorGlobal","La bestia seleccionada no existe");
				rd.forward(request, response);
				return;
			}catch(Exception e) {
				logger.log(Level.WARNING, "Error al obtener la bestia seleccionada en el servlet MapaBestias", e);
				request.setAttribute("errorGlobal","No se ha podido obtener la bestia seleccionada");
				rd.forward(request, response);
				return;
			}
			request.setAttribute("selectedBestia", bestia);
		}

		LinkedList<Bestia> bestias = new LinkedList<>();

		try{
			bestias = controladorBestia.findAll();
			bestias.sort((b1, b2) -> {
				if (b1.getNombre() == null) return -1;
				if (b2.getNombre() == null) return 1;
				return b1.getNombre().compareToIgnoreCase(b2.getNombre());
			});
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener la lista de bestias en el servlet MapaBestias", e);
			request.setAttribute("errorGlobal","No se ha podido obtener la lista de bestias");
			rd.forward(request, response);
			return;
		}
		Map<Bestia, String> bestiasImagenes = new LinkedHashMap<>();
		Registro registro = null;
		for(Bestia b: bestias) {
			try {
				registro = controladorRegistro.getRegistroToShow(b, LocalDateTime.now());
			} catch(Exception e) {
				logger.log(Level.WARNING, "Error al obtener el registro de la bestia en el servlet MapaBestias", e);
				request.setAttribute("errorGlobal","No se ha podido obtener el registro de la bestia");
			}
			bestiasImagenes.put(b, registro != null? registro.getMainPic(): EnvHelper.get("DEFAULT_PICTURE_ID"));
		}
		request.getSession().setAttribute("bestias", bestiasImagenes);

		if(bestias.isEmpty()) {
			request.setAttribute("errorGlobal","No hay bestias para mostrar");
		}
		
		rd.forward(request, response);
	}

	

}
