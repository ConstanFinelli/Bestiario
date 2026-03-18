package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicBestia;

import java.io.IOException;

import entities.Bestia;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CrearBestia
 */
@WebServlet("/bestias/crear")
public class CrearBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// CONTROLADORES
	private LogicBestia controlador = new LogicBestia();
	

    public CrearBestia() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.BESTIA_FORMS_JSP("")); 
		HttpSession session = request.getSession();
		
		String nombre = request.getParameter("nombre");
		String peligrosidad= request.getParameter("peligrosidad");
		Usuario usuario = (Usuario) session.getAttribute("user");
		
		try {
			if (nombre != null && peligrosidad != null) {
				String estado = "pendiente";
				if (usuario.getEstado().equals("investigador")) {
					estado = "aprobado";
				} 
				Bestia bestia = new Bestia(nombre, peligrosidad, estado);
				bestia = controlador.save(bestia);
			} 
		} catch (NumberFormatException e) {
			e.getMessage();
		} 
	
		rd.forward(request, response);
	}
}
