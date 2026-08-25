package servlet.categoria;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCategoria;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Categoria;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarCategoria
 */
@WebServlet("/categorias/eliminar")
public class EliminarCategoria extends HttpServlet {
	private LogicCategoria controlador = new LogicCategoria();
	private static final Logger logger = Logger.getLogger(EliminarCategoria.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarCategoria() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=categorias");
		Categoria cat = new Categoria(Integer.parseInt(id),null, null);
		try {
			cat = controlador.delete(cat);
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error crítico al eliminar una categoría en el servlet EliminarCategoria", e);
			request.setAttribute("errorGlobal", "No se ha podido eliminar la categoría.");
		}
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage", "¡Categoría eliminada con éxito!");
		}
		rd.forward(request, response);
	}

}
