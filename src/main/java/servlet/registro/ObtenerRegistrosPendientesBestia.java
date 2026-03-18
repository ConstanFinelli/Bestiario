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
import java.util.LinkedList;

import entities.Bestia;
import entities.Registro;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ObtenerBestiaConRegistrosPendientes
 */
@WebServlet("/registros/obtenerRegistrosPendientesBestia")
public class ObtenerRegistrosPendientesBestia extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	
	private static final long serialVersionUID = 1L;
       
    public ObtenerRegistrosPendientesBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTROS_PENDIENTES_JSP(""));
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id));
		bestia = controlador.getOne(bestia);
		LinkedList<Registro> registros = null;
		registros = controladorRegistro.findRegistrosPendientes(bestia);
		request.setAttribute("foundBestia", bestia);
		request.setAttribute("foundRegistros", registros);
		rd.forward(request, response);
	}


}
