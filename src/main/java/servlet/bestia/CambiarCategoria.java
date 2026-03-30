package servlet.bestia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicCategoria;

import java.io.IOException;
import java.util.LinkedList;

import entities.Bestia;
import entities.Categoria;
import entities.Habitat;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CambiarCategoria
 */
@WebServlet("/bestias/cambiarCategoria")
public class CambiarCategoria extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicCategoria controladorCategoria = new LogicCategoria();
	
	private static final long serialVersionUID = 1L;
       
    public CambiarCategoria() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Bestia bestia = controlador.getOne(new Bestia(Integer.parseInt(id), null, null, null));
		String idCategoria = request.getParameter("idCategoria");
		Categoria cat = controladorCategoria.getOne(new Categoria(Integer.parseInt(idCategoria),null,null));
		LinkedList<Categoria> categoriasBestia = bestia.getCategorias();
		boolean isIn = false;
		for(Categoria categoria:categoriasBestia) {
			if(categoria.getIdCategoria() == cat.getIdCategoria()) {
				isIn = true;
		}
		}
		if(!isIn) {
			categoriasBestia.add(cat);
			bestia.setCategorias(categoriasBestia);
			controlador.saveCategorias(bestia);
		}else {
			controlador.removeRelation(bestia, cat);
			bestia.getCategorias().remove(cat);
		}
		request.getSession().setAttribute("bestia", bestia);
		request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP("")).forward(request, response);
	}

}
