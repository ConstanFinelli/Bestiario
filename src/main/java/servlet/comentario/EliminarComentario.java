package servlet.comentario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicComentario;
import entities.Bestia;
import entities.Comentario;
import entities.Usuario;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/comentarios/eliminar")
public class EliminarComentario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EliminarComentario.class.getName());
	private LogicComentario controladorComentario = new LogicComentario();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorGlobal","Metodo GET no permitido para esta operación.");
		request.getRequestDispatcher(HttpRoutes.HOME_JSP("")).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (session != null) ? (Usuario) session.getAttribute("user") : null;

		if (usuario == null || !"investigador".equals(usuario.getEstado())) {
			logger.log(Level.WARNING, "Acceso no autorizado al servlet EliminarComentario");
			request.setAttribute("errorGlobal", "Acceso denegado: se requieren permisos de investigador.");
			request.getRequestDispatcher(HttpRoutes.HOME_JSP("")).forward(request, response);
			return;
		}

		String idUsuarioStr = request.getParameter("idUsuario");
		String idBestiaStr = request.getParameter("idBestia");
		String fechaPublicacionStr = request.getParameter("fechaPublicacion");
		String nroRegistroStr = request.getParameter("nroRegistro");

		try {
			int idUsuario = Integer.parseInt(idUsuarioStr);
			int idBestia = Integer.parseInt(idBestiaStr);
			LocalDateTime fechaPublicacion = LocalDateTime.parse(fechaPublicacionStr);

			Usuario publicador = new Usuario(idUsuario);
			Bestia bestia = new Bestia(idBestia);
			Comentario comentario = new Comentario(publicador, bestia, fechaPublicacion, "");

			controladorComentario.delete(comentario);
		} catch (NumberFormatException | DateTimeParseException e) {
			logger.log(Level.WARNING, "Error al parsear parámetros en EliminarComentario", e);
			request.setAttribute("errorGlobal", "Error al parsear parámetros.");
		} catch (DataNotFoundException e) {
			logger.log(Level.WARNING, "Comentario no encontrado para eliminar: {0}", e.getMessage());
			request.setAttribute("errorGlobal", "Comentario no encontrado para eliminar.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error inesperado al eliminar comentario", e);
			request.setAttribute("errorGlobal", "Error inesperado al eliminar comentario.");
		}
		request.getRequestDispatcher(HttpRoutes.OBTENER_REGISTRO_BESTIA("") + "?id=" + idBestiaStr + "&nroRegistro=" + nroRegistroStr).forward(request, response);
	}
}
