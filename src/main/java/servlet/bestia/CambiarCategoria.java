package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicCategoria;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Categoria;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CambiarCategoria
 */
@WebServlet("/bestias/cambiarCategoria")
public class CambiarCategoria extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicCategoria controladorCategoria = new LogicCategoria();
	private static final Logger logger = Logger.getLogger(CambiarCategoria.class.getName());

	
	private static final long serialVersionUID = 1L;
       
    public CambiarCategoria() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP(""));
		String id = request.getParameter("id");
		Bestia bestia = null;
		Categoria cat = null;
		try {
			bestia = controlador.getOne(new Bestia(Integer.parseInt(id), null, null, null));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener bestia en el servlet CambiarCategoria", e);
			request.setAttribute("errorGlobal", "No se ha podido obtener la bestia seleccionada");
			rd.forward(request, response);
			return;
		}
		String idCategoria = request.getParameter("idCategoria");
		try {
			cat = controladorCategoria.getOne(new Categoria(Integer.parseInt(idCategoria),null,null));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al obtener habitat de la bestia seleccionada en el servlet CambiarCategoria", e);
			request.setAttribute("errorGlobal", "No se ha podido obtener el habitat de la bestia seleccionada");
			rd.forward(request, response);
			return;
		}
		
		if(bestia != null && cat != null) {
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
				try {
					controlador.saveCategorias(bestia);
				}catch(Exception e) {
					logger.log(Level.WARNING, "Error al guardar categoria en la bestia en el servlet CambiarCategoria", e);
					request.setAttribute("errorGlobal", "No se ha podido guardar la categoria en la bestia seleccionada");
					rd.forward(request, response);
					return;
				}
			}else {
				try {
					controlador.removeRelation(bestia, cat);
				}catch(Exception e) {
					logger.log(Level.WARNING, "Error al remover categoria en la bestia en el servlet CambiarCategoria", e);
					request.setAttribute("errorGlobal", "No se ha podido remover la categoria en la bestia seleccionada");
					rd.forward(request, response);
					return;
				}
				bestia.getCategorias().remove(cat);
			}
		}
		if(request.getAttribute("errorGlobal") == null) {
			request.setAttribute("feedbackMessage","¡Categoría actualizada con éxito!");
		}
		request.getSession().setAttribute("bestia", bestia);
		rd.forward(request, response);	
	}

}
