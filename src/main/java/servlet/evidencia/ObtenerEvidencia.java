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

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ObtenerEvidencia
 */
@WebServlet("/evidencias/obtener")
public class ObtenerEvidencia extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObtenerEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP(""));
		String idTipo = request.getParameter("IdTipo");
		String nroEvidencia = request.getParameter("nroEvidencia");
		TipoEvidencia t = new TipoEvidencia(Integer.parseInt(idTipo));
		t = controladorTipoEvidencia.getOne(t);
		Evidencia e = new Evidencia(Integer.parseInt(nroEvidencia), null, null, null, t);
		e = controladorEvidencia.getOne(e);
		request.setAttribute("gottenEvidencia", e);
		rd.forward(request, response);
	}


}
