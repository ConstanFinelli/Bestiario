package servlet.comentario;

import jakarta.servlet.RequestDispatcher;
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
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AgregarComentario() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTRO_JSP(""));
		String contenido = request.getParameter("contenido");
		String idUsuario = request.getParameter("idUsuario");
		String idBestia = request.getParameter("idBestia");
		if(contenido != null && idUsuario != null && idBestia != null) {
			LocalDateTime fechaComentario = LocalDateTime.now();
			Usuario publicador = controladorUsuario.getOne(new Usuario(Integer.parseInt(idUsuario)));
			Bestia bestia = controladorBestia.getOne(new Bestia(Integer.parseInt(idBestia),null,null, null));
			Comentario comentario = new Comentario(publicador, bestia, fechaComentario, contenido);
			controladorComentario.save(comentario);
		}
		rd.forward(request, response);
	}

}
