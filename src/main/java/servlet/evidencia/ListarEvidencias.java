package servlet.evidencia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEvidencia;

import java.io.IOException;
import java.util.LinkedList;

import entities.Evidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarEvidencias
 */
@WebServlet("/evidencias/listar")
public class ListarEvidencias extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarEvidencias() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP(""));
		LinkedList<Evidencia> evidencias = controladorEvidencia.findAll();
		request.setAttribute("gottenEvidencias", evidencias);
		rd.forward(request, response);
	}

	
}


