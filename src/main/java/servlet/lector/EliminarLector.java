package servlet.lector;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCategoria;
import logic.LogicUsuario;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Categoria;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarCategoria
 */
@WebServlet("/lectores/eliminar")
public class EliminarLector extends HttpServlet {
	private LogicUsuario controlador = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(EliminarLector.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarLector() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String feedbackMessage = "";
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=usuarios");
		Usuario us = null;
		try{
			us = new Usuario(Integer.parseInt(id),null, null,null);
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear id en el servlet EliminarLector", e);
			request.setAttribute("errorGlobal", "El id del usuario no es válido. ");
			rd.forward(request, response);
			return;
		}
		try{
			us = (Usuario) controlador.getOne(us);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir usuario en el servlet EliminarLector", e);
			request.setAttribute("errorGlobal", "No se ha podido conseguir el usuario. ");
			rd.forward(request, response);
			return;
		}
		try{
			us = (Usuario) controlador.delete(us);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al eliminar usuario en el servlet EliminarLector", e);
			request.setAttribute("errorGlobal", "No se ha podido eliminar el usuario. ");
		}
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage","¡Usuario eliminado con éxito!");
		}
		rd.forward(request, response);
	}

}
