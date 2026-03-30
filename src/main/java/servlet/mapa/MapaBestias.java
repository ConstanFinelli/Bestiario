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
import java.util.LinkedList;
import java.util.Map;

import entities.Bestia;
import entities.Registro;
import helpers.EnvHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class MapaBestias
 */
@WebServlet("/mapas/bestias")
public class MapaBestias extends HttpServlet {
	LogicBestia controladorBestia = new LogicBestia();
	LogicRegistro controladorRegistro = new LogicRegistro();
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
			Bestia bestia = new Bestia(Integer.parseInt(selectedId));
			bestia = controladorBestia.getOne(bestia);
			request.setAttribute("selectedBestia", bestia);
		}
		LinkedList<Bestia> bestias = controladorBestia.findAll();
		Map<Bestia, String> bestiasImagenes = new HashMap<>();
		Registro registro = null;
		for(Bestia b: bestias) {
			registro = controladorRegistro.getRegistroToShow(b, LocalDateTime.now());
			bestiasImagenes.put(b, registro != null? registro.getMainPic(): EnvHelper.get("DEFAULT_PICTURE_ID"));
		}
		request.getSession().setAttribute("bestias", bestiasImagenes);
		rd.forward(request, response);
	}

	

}
