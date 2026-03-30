package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

import java.io.IOException;
import java.time.LocalDateTime;

import entities.Bestia;

/**
 * Servlet implementation class ActualizarBestia
 */
@WebServlet("/bestias/actualizar")
public class ActualizarBestia extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarBestia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String peligrosidad = request.getParameter("peligrosidad");
		String estado = request.getParameter("estado");
		Bestia bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad, estado);
		String referer = request.getHeader("referer");
		String toRedirect = referer.substring(referer.indexOf(request.getContextPath())); // hacerlo en un helper
		String imagen = controladorRegistro.getImagen(bestia, LocalDateTime.now());
		bestia = controlador.update(bestia);	
		request.getSession().setAttribute("updateBestia", bestia);		
		request.getSession().setAttribute("imagen", imagen);
		response.sendRedirect(toRedirect);;
	}

	}

