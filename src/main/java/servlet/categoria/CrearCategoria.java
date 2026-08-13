package servlet.categoria;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCategoria;
import servlet.bestia.ActualizarBestia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Categoria;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CrearCategoria
 */
@WebServlet("/categorias/crear")
public class CrearCategoria extends HttpServlet {
	private LogicCategoria controlador = new LogicCategoria();
	private static final Logger logger = Logger.getLogger(CrearCategoria.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    public CrearCategoria() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("nombre");
		String desc = request.getParameter("descripcion");
		String feedbackMessage = "";
		Categoria cat = new Categoria(0, name, desc);
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=categorias");
		try {
			cat = controlador.save(cat);
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error crítico al crear una categoría en el servlet CrearCategoría", e);
			request.setAttribute("errorGlobal", "No se ha podido crear la categoría.");
		}
		if(cat != null) {
			feedbackMessage = "¡Categoría creada con éxito!";
		}
		request.setAttribute("feedbackMessage", feedbackMessage);
		rd.forward(request, response);
	}

}
