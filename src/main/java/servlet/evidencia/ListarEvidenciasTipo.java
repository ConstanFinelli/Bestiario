package servlet.evidencia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEvidencia;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.util.LinkedList;

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarEvidenciasTipo
 */
@WebServlet("/evidencias/listarPorTipo")
public class ListarEvidenciasTipo extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarEvidenciasTipo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP(""));
		String idTipo = request.getParameter("idTipo");
		TipoEvidencia tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(idTipo)));
		LinkedList<Evidencia> evidencias = null;
		if(tipo != null) {
			evidencias = controladorEvidencia.findAllType(tipo);
		}
		request.setAttribute("gottenEvidencias", evidencias);
		rd.forward(request, response);
	}


}
