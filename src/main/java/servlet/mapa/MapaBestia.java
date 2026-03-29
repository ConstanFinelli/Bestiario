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

import entities.Bestia;
import entities.Registro;
import helpers.HttpRoutes;

/**
 * Servlet implementation class MapaBestia
 */
@WebServlet("/mapas/bestia")
public class MapaBestia extends HttpServlet {
	LogicBestia controladorBestia = new LogicBestia();
	LogicRegistro controladorRegistro = new LogicRegistro();
	
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
		Bestia bestia = new Bestia(Integer.parseInt(idBestia));
		bestia = controladorBestia.getOne(bestia);
		Registro registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		Map<Bestia, String> bestias = new HashMap<Bestia, String>();
		bestias.put(bestia, registro.getMainPic());
		request.setAttribute("bestias", bestias);
	
		rd.forward(request, response);
	}

}
