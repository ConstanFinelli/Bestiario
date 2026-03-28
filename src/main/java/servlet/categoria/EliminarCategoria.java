package servlet.categoria;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCategoria;

import java.io.IOException;

import entities.Categoria;
import helpers.HttpRoutes;

/**
 * Servlet implementation class EliminarCategoria
 */
@WebServlet("/categorias/eliminar")
public class EliminarCategoria extends HttpServlet {
	private LogicCategoria controlador = new LogicCategoria();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarCategoria() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String feedbackMessage = "";
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=categorias");
		Categoria cat = new Categoria(Integer.parseInt(id),null, null);
		cat = controlador.delete(cat);
		if(cat == null) {
			feedbackMessage = "¡No se ha podido eliminar la categoría!";
		}else {
			feedbackMessage = "¡Categoría eliminada con éxito!";
		}
		request.setAttribute("feedbackMessage", feedbackMessage);
		rd.forward(request, response);
	}

}
