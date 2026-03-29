package servlet.lector;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCategoria;
import logic.LogicUsuario;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import entities.Categoria;
import entities.Lector;
import entities.Usuario;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarCategorias
 */
@WebServlet("/lectores/listar")
public class ListarLectores extends HttpServlet {
	private LogicUsuario controlador = new LogicUsuario();
	
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
		usuarios = controlador.findAll();
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
