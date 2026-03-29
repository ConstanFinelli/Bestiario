package servlet.noticia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicNoticia;

import java.io.IOException;
import java.time.LocalDateTime;

import entities.Investigador;
import entities.Noticia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CrearNoticia
 */
@WebServlet("/noticias/crear")
public class CrearNoticia extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private LogicNoticia controlador = new LogicNoticia(); 
    
    public CrearNoticia() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contenido = request.getParameter("contenido");
		String titulo = request.getParameter("titulo");
		
		HttpSession session = request.getSession();
		
		Investigador publicador = (Investigador) session.getAttribute("user");
		
		Noticia noticia = new Noticia(0, titulo, contenido, LocalDateTime.now(), publicador);
		
		if(titulo != null && contenido != null) {
			controlador.save(noticia);
			response.sendRedirect(HttpRoutes.LISTAR_NOTICIAS(request.getContextPath()));	}
	}

}
