package servlet.categoria;

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
 * Servlet implementation class ObtenerCategoria
 */
@WebServlet("/categorias/obtener")
public class ObtenerCategoria extends HttpServlet {
	private LogicCategoria controlador = new LogicCategoria();
	private static final Logger logger = Logger.getLogger(ActualizarBestia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObtenerCategoria() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Categoria cat = new Categoria(Integer.parseInt(id),null, null);
		try {
			cat = controlador.getOne(cat);
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error crítico al obtener una categoría en el servlet ObtenerCategoría", e);
			request.setAttribute("errorGlobal", "No se ha podido obtener la categoría. ");
		}
		request.setAttribute("foundCategoria", cat);
		
		request.getRequestDispatcher(HttpRoutes.CATEGORIA_FORM_JSP("")).forward(request, response);
	}


}
