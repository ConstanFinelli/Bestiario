package servlet.evidencia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEvidencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Evidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarEvidencias
 */
@WebServlet("/evidencias/listar")
public class ListarEvidencias extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private static final Logger logger = Logger.getLogger(ListarEvidencias.class.getName());
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
		List<String> errores = new ArrayList<>();
		LinkedList<Evidencia> evidencias = null;
		try {
			evidencias = controladorEvidencia.findAll();
		}catch(Exception e){
			logger.log(Level.WARNING, "Error buscando las evidencias a listar en el servlet ListarEvidencias", e);
			errores.add("No se han podido buscar las evidencias");
		}
		
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde");
			request.setAttribute("errorGlobal", errores);
		}
		request.setAttribute("gottenEvidencias", evidencias);
		rd.forward(request, response);
	}

	
}


