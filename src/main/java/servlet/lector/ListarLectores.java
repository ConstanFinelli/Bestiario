package servlet.lector;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Lector;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarCategorias
 */
@WebServlet("/lectores/listar")
public class ListarLectores extends HttpServlet {
	private LogicUsuario controlador = new LogicUsuario();
	private static final Logger logger = Logger.getLogger(ListarLectores.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarLectores() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LinkedList<Usuario> usuarios = new LinkedList<>();
		RequestDispatcher rd = null;
		try{
			usuarios = controlador.findAll();
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir usuarios en el servlet ListarLectores", e);
			request.setAttribute("errorGlobal", "No se ha podido conseguir los usuarios. ");
		}
		LinkedList<Lector> lectores = usuarios.stream().filter(u -> u.getEstado().contains("lector"))
			    .filter(u -> u instanceof Lector).map(u -> (Lector) u).collect(Collectors.toCollection(LinkedList::new));
		request.setAttribute("foundLectores", lectores);
		
		rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=usuarios");
		
		rd.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	
}
