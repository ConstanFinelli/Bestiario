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

@WebServlet("/comentarios/editar")
public class EditarComentario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(EditarComentario.class.getName());
	private LogicComentario controladorComentario = new LogicComentario();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("errorGlobal", "Metodo GET no permitido para esta operación.");
		request.getRequestDispatcher(HttpRoutes.HOME_JSP("")).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idBestiaStr = request.getParameter("idBestia");
		String fechaPublicacionStr = request.getParameter("fechaPublicacion");
		String nroRegistroStr = request.getParameter("nroRegistro");
		String contenido = request.getParameter("contenido");

		HttpSession session = request.getSession(false);
		Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("user") : null;

		if (usuarioSesion == null) {
			request.setAttribute("errorGlobal", "Debe iniciar sesión para editar un comentario.");
			request.getRequestDispatcher(HttpRoutes.LOGIN_JSP("")).forward(request, response);
			return;
		}

		String target = HttpRoutes.OBTENER_REGISTRO_BESTIA("") + "?id=" + idBestiaStr;
		if (nroRegistroStr != null && !nroRegistroStr.isBlank()) {
			target += "&nroRegistro=" + nroRegistroStr;
		}

		try {
			if (contenido == null || contenido.isBlank() || contenido.trim().length() > 200) {
				request.setAttribute("errorGlobal", "El contenido del comentario es inválido o supera los 200 caracteres.");
			} else {
				int idBestia = Integer.parseInt(idBestiaStr);
				LocalDateTime fechaPublicacion = LocalDateTime.parse(fechaPublicacionStr);

				Bestia bestia = new Bestia(idBestia);
				Comentario comentario = new Comentario(usuarioSesion, bestia, fechaPublicacion, contenido.trim());

				controladorComentario.update(comentario);
			}
		} catch (NumberFormatException | DateTimeParseException e) {
			logger.log(Level.WARNING, "Error al parsear parámetros en EditarComentario", e);
			request.setAttribute("errorGlobal", "Error al parsear parámetros.");
		} catch (DataNotFoundException e) {
			logger.log(Level.WARNING, "Comentario no encontrado para editar en EditarComentario: {0}", e.getMessage());
			request.setAttribute("errorGlobal", "No se encontró el comentario o no tienes permiso para editarlo.");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error inesperado al editar comentario en EditarComentario", e);
			request.setAttribute("errorGlobal", "Error inesperado al editar comentario.");
		}

		request.getRequestDispatcher(target).forward(request, response);
	}
}
