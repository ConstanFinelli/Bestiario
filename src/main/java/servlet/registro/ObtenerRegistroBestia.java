package servlet.registro;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

import java.io.IOException;

import entities.Bestia;
import entities.Registro;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ObtenerBestia
 */
@WebServlet("/registros/obtenerRegistroConBestia")
public class ObtenerRegistroBestia extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	
	private static final long serialVersionUID = 1L;
     
    public ObtenerRegistroBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTRO_JSP(""));
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id));
		bestia = controlador.getOne(bestia);
		String nroRegistro = request.getParameter("nroRegistro");
		Registro registro = null;
		
		registro = new Registro(Integer.parseInt(nroRegistro), null, null, null, null, null, null, bestia);
		registro = controladorRegistro.getOne(registro);
			
		request.setAttribute("foundBestia", bestia);
		request.setAttribute("foundRegistro", registro);
		rd.forward(request, response);
	}

}
