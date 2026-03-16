package servlet.evidencia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicTipoEvidencia;

import java.io.IOException;

import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ObtenerTipoEvidencia
 */
@WebServlet("/evidencias/obtenerTipoEvidencia")
public class ObtenerTipoEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObtenerTipoEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.TIPO_EVIDENCIA_FORM_JSP(""));
		String id = request.getParameter("id");
		TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), null);
		tipo = controlador.getOne(tipo);
		request.setAttribute("gottenTipo", tipo);
		rd.forward(request, response);
	}

}
