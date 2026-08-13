package servlet.comentario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicComentario;
import logic.LogicUsuario;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Comentario;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class AgregarComentario
 */
@WebServlet("/comentarios/agregar")
public class AgregarComentario extends HttpServlet {
	private LogicComentario controladorComentario = new LogicComentario();
	private LogicBestia controladorBestia = new LogicBestia();
	private LogicUsuario controladorUsuario = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(AgregarComentario.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AgregarComentario() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contenido = request.getParameter("contenido");
		String idUsuario = request.getParameter("idUsuario");
		String idBestia = request.getParameter("idBestia");
		String nroRegistro = request.getParameter("nroRegistro");
		List<String> errores = new ArrayList<>();
		
		if(contenido != null && idUsuario != null && idBestia != null) {
			LocalDateTime fechaComentario = LocalDateTime.now();
			Usuario publicador = null;
			Bestia bestia = null;
			try {
				publicador = controladorUsuario.getOne(new Usuario(Integer.parseInt(idUsuario)));
			}catch(Exception e) {
				logger.log(Level.WARNING, "Error al conseguir publicador del comentario a agregar en el servlet AgregarComentario", e);
				errores.add("No se ha podido conseguir el publicador del comentario a agregar");
			}
			try {
				bestia = controladorBestia.getOne(new Bestia(Integer.parseInt(idBestia),null,null, null));
			}catch(Exception e) {
				logger.log(Level.WARNING, "Error al conseguir la bestia asociada al comentario a agregar en el servlet AgregarComentario", e);
				errores.add("No se ha podido conseguir la bestia asociada al comentario a agregar");
			}
			Comentario comentario = new Comentario(publicador, bestia, fechaComentario, contenido);
			try {
				controladorComentario.save(comentario);
			}catch(Exception e) {
				logger.log(Level.WARNING, "Error al crear comentario en el servlet AgregarComentario", e);
				errores.add("No se ha podido crear el comentario");
			}
		}
		if(!errores.isEmpty()) {
			errores.add("");
			request.setAttribute("errorGlobal", errores);
		}
		response.sendRedirect(HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) + "?id=" + idBestia + "&nroRegistro=" + nroRegistro + "#comentarios");
	}

}
