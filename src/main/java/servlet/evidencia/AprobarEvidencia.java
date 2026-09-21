package servlet.evidencia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Usuario;
import exceptions.DataNotFoundException;
import helpers.HttpRoutes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicEvidencia;

/**
 * Servlet implementation class AprobarEvidencia
 */
@WebServlet("/evidencias/aprobar")
public class AprobarEvidencia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private static final Logger logger = Logger.getLogger(AprobarEvidencia.class.getName());

	public AprobarEvidencia() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("user");
		if (usuario == null || !"investigador".equals(usuario.getEstado())) {
			response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
			return;
		}

		String nroEvidenciaStr = request.getParameter("nroEvidencia");
		String idTipoStr = request.getParameter("idTipoEvidencia");
		String idBestia = request.getParameter("idBestia");

		try {
			int nroEvidencia = Integer.parseInt(nroEvidenciaStr);
			int idTipo = Integer.parseInt(idTipoStr);
			controladorEvidencia.updateEstado(nroEvidencia, idTipo, "aprobado");
		} catch (NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear parámetros en AprobarEvidencia", e);
		} catch (DataNotFoundException e) {
			logger.log(Level.WARNING, "Evidencia no encontrada en AprobarEvidencia", e);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error al aprobar la evidencia en AprobarEvidencia", e);
		}

		response.sendRedirect(HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) + "?id=" + idBestia);
	}

}

