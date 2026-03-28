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
 * Servlet implementation class ActualizarCategoria
 */
@WebServlet("/categorias/actualizar")
public class ActualizarCategoria extends HttpServlet {
	private LogicCategoria controlador = new LogicCategoria();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarCategoria() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=categorias");
		String id = request.getParameter("id");
		String name = request.getParameter("nombre");
		String desc = request.getParameter("descripcion");
		String feedbackMessage = "";
		Categoria cat = new Categoria(Integer.parseInt(id), name, desc);
		cat = controlador.update(cat);
		if(cat == null) {
			feedbackMessage = "¡No se ha podido actualizar la categoría!";
		}else {
			feedbackMessage = "¡Categoría actualizada con éxito!";
		}
		request.setAttribute("feedbackMessage", feedbackMessage);
		request.setAttribute("updatedCategoria", cat);
		rd.forward(request, response);
	}

}
