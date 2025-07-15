package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicHabitat;

import java.io.IOException;
import java.util.LinkedList;

import entities.Habitat;

/**
 * Servlet implementation class SvHabitat
 */
@WebServlet("/SvHabitat")
public class SvHabitat extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private LogicHabitat controlador = new LogicHabitat();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if(id != null) {
			Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
			ht = controlador.getOne(ht);
			if(ht !=null){
			response.getWriter().append("<div>" + ht + "</div>");
			}else {
				response.getWriter().append("Habitat no encontrada");
			}
		}else {
			LinkedList<Habitat> hts = new LinkedList<>();
			hts = controlador.findAll();
			for(Habitat ht : hts) {
				response.getWriter().append("<div>" + ht + "</div>");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		String localizacion = request.getParameter("localizacion");
		if(nombre != null && localizacion != null) {
			Habitat ht = new Habitat(0, nombre, null, localizacion);
			ht = controlador.save(ht);
			response.getWriter().append("<div>" + ht + "</div>");
		}else {
			response.getWriter().append("Por favor ingresar un nombre y/o descripcion válidos.");
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		if(id != null) {
			Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
			ht = controlador.delete(ht);
			response.getWriter().append("<div>"+ ht +"</div>");
		}else {
			response.getWriter().append("Habitat no encontrada");
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String localizacion = request.getParameter("localizacion");
		
		if(id != null && nombre != null && localizacion != null) {
			Habitat ht = new Habitat(Integer.parseInt(id), nombre, null, localizacion);
			ht = controlador.update(ht);
			response.getWriter().append("<div>"+ ht +"</div>");
		}else {
			response.getWriter().append("Habitat no encontrada");
		}
	}
	
}
