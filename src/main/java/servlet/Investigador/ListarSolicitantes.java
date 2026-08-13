package servlet.Investigador;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Investigador;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarSolicitantes
 */
@WebServlet("/investigadores/listarSolicitantes")
public class ListarSolicitantes extends HttpServlet {
	private LogicUsuario controladorUsuario = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(ListarSolicitantes.class.getName());
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarSolicitantes() {
        super();
        
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(""));
		try{
			LinkedList<Investigador> solicitantes = controladorUsuario.findAllSolicitantes();
			if(solicitantes.size() != 0) {
				request.setAttribute("solicitantes", solicitantes);
			} else {
				request.setAttribute("errorMsg", "No hay candidaturas en este momento");
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error al listar solicitantes en el servlet ListarSolicitantes", e);
			request.setAttribute("errorGlobal", "No se han podido listar solicitantes");
		}
		rd.forward(request, response);
	}

}
