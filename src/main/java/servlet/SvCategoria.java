package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

import entities.Categoria;
import entities.TipoEvidencia;
import logic.LogicCategoria;

/**
 * Servlet implementation class SvCategoria
 */
@WebServlet("/SvCategoria")
public class SvCategoria extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public LogicCategoria controlador = new LogicCategoria();
    /**
     * Default constructor. 
     */
    public SvCategoria() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");		if(id != null) {
			Categoria cat = new Categoria(Integer.parseInt(id), null);
			Categoria categoria = controlador.getOne(cat);
			if(categoria != null) {
				response.getWriter().append("Id: " + categoria.getIdCategoria() + "<br>Descripción: " +  categoria.getDescripcionCategoria());
			}else {
				response.getWriter().append("La categoria no se ha encontrado");
			}
		}else {
			LinkedList<Categoria> categorias = new LinkedList<>();
			categorias = controlador.findAll();
			for(Categoria cat : categorias) {
				response.getWriter().append("Id: " + cat.getIdCategoria() + "<br>Descripción: " +  cat.getDescripcionCategoria() + "<br>");
			}
		}
	}
		

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
