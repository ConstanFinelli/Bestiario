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

import entities.Categoria;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarCategoria
 */
@WebServlet("/lectores/eliminar")
public class EliminarLector extends HttpServlet {
	private LogicUsuario controlador = new LogicUsuario();
	
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
		Usuario us = new Usuario(Integer.parseInt(id),null, null,null);
		us = controlador.delete(us);
		if(us == null) {
			feedbackMessage = "¡No se ha podido eliminar el usuario!";
		}else {
			feedbackMessage = "¡Usuario eliminada con éxito!";
		}
		request.setAttribute("feedbackMessage", feedbackMessage);
		rd.forward(request, response);
	}

}
