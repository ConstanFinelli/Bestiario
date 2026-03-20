package servlet.categoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCategoria;

import java.io.IOException;

import entities.Categoria;

/**
 * Servlet implementation class CrearCategoria
 */
@WebServlet("/categorias/crear")
public class CrearCategoria extends HttpServlet {
	private LogicCategoria controlador = new LogicCategoria();
	
	private static final long serialVersionUID = 1L;
       
    public CrearCategoria() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("nombre");
		String desc = request.getParameter("descripcion");
		Categoria cat = new Categoria(0, name, desc);
		cat = controlador.save(cat);
		request.setAttribute("createdCategoria", cat);
	}

}
