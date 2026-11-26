package servlet.noticia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Investigador;
import entities.Noticia;
import entities.Usuario;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicNoticia;

@WebServlet("/noticias/editar")
public class EditarNoticia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicNoticia controlador = new LogicNoticia();
	private static final Logger logger = Logger.getLogger(EditarNoticia.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorGlobal", "Metodo GET no permitido para esta operación.");
		request.getRequestDispatcher(HttpRoutes.LISTAR_NOTICIAS("")).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("user") : null;

		if (usuarioSesion == null || !"investigador".equals(usuarioSesion.getEstado())) {
			request.setAttribute("errorGlobal", "No tienes permiso para editar noticias.");
			request.getRequestDispatcher(HttpRoutes.LISTAR_NOTICIAS("")).forward(request, response);
			return;
		}

		String idStr = request.getParameter("idNoticia");
		String titulo = request.getParameter("titulo");
		String contenido = request.getParameter("contenido");

		try {
			int idNoticia = Integer.parseInt(idStr);
			if (titulo == null || titulo.isBlank() || contenido == null || contenido.isBlank()) {
				request.setAttribute("errorGlobal", "El título y el contenido no pueden estar vacíos.");
			} else {
				Noticia actual = controlador.getOne(new Noticia(idNoticia));
				if (actual.getPublicador() == null || actual.getPublicador().getIdUsuario() != usuarioSesion.getIdUsuario()) {
					logger.log(Level.WARNING, "Intento de edición sin permiso en EditarNoticia por usuario {0}", usuarioSesion.getIdUsuario());
					request.setAttribute("errorGlobal", "Solo quien publicó la noticia puede editarla.");
				} else {
					Noticia noticiaActualizada = new Noticia(idNoticia, titulo.trim(), contenido.trim(), null, (Investigador) usuarioSesion);
					controlador.update(noticiaActualizada);
				}
			}
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear idNoticia en EditarNoticia", e);
			request.setAttribute("errorGlobal", "La id ingresada no es válida.");
		} catch (DataNotFoundException e) {
			logger.log(Level.WARNING, "Noticia no encontrada en EditarNoticia: {0}", e.getMessage());
			request.setAttribute("errorGlobal", "La noticia a editar no existe.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error inesperado al editar noticia en EditarNoticia", e);
			request.setAttribute("errorGlobal", "No se ha podido editar la noticia.");
		}

		request.getRequestDispatcher(HttpRoutes.LISTAR_NOTICIAS("")).forward(request, response);
	}
}
