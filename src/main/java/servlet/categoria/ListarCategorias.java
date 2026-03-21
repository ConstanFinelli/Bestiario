package servlet.categoria;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCategoria;

import java.io.IOException;
import java.util.LinkedList;

import entities.Categoria;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarCategorias
 */
@WebServlet("/categorias/listar")
public class ListarCategorias extends HttpServlet {
	private LogicCategoria controlador = new LogicCategoria();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarCategorias() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LinkedList<Categoria> categorias = new LinkedList<>();
		String flag = request.getParameter("flag");
		RequestDispatcher rd = null;
		categorias = controlador.findAll();
		request.setAttribute("foundCategorias", categorias);
		
		if(flag.equals("listaBestias")) {
			rd = request.getRequestDispatcher(HttpRoutes.BESTIA_LIST_JSP(""));
		}else {
			rd = request.getRequestDispatcher(HttpRoutes.CATEGORIA_FORM_JSP(""));
		}
		
		rd.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	
}
