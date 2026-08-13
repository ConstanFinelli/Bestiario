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
import java.util.logging.Logger;
import java.util.logging.Level;

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
	private static final Logger logger = Logger.getLogger(ObtenerRegistrosPendientesBestia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    public ObtenerRegistrosPendientesBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTROS_PENDIENTES_JSP(""));
		String id = request.getParameter("id");
		try{
			Bestia bestia = new Bestia(Integer.parseInt(id));
			bestia = controlador.getOne(bestia);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear id en el servlet ObtenerRegistrosPendientesBestia", e);
			request.setAttribute("errorGlobal", "La id de la bestia es inválida. ");
			return;
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir bestia en el servlet ObtenerRegistrosPendientesBestia", e);
			request.setAttribute("errorGlobal", "No se ha conseguido la bestia. ");
			return;
		}
		LinkedList<Registro> registros = null;
		try{
			registros = controladorRegistro.findRegistrosPendientes(bestia);
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir registros pendientes en el servlet ObtenerRegistrosPendientesBestia", e);
			request.setAttribute("errorGlobal", "No se han conseguido los registros pendientes. ");
			return;
		}
		request.setAttribute("foundBestia", bestia);
		request.setAttribute("foundRegistros", registros);
		rd.forward(request, response);
	}


}
