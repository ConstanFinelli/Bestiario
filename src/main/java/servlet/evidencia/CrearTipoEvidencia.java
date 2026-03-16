package servlet.evidencia;

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
 * Servlet implementation class CrearTipoEvidencia
 */
@WebServlet("/evidencias/crearTipoEvidencia")
public class CrearTipoEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearTipoEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String desc = request.getParameter("descripcion");
			TipoEvidencia tipo = new TipoEvidencia(0, null);
			tipo.setDescripcion(desc);
			tipo = controlador.save(tipo);
			request.setAttribute("gottenTipo", tipo);
			request.getRequestDispatcher(HttpRoutes.TIPO_EVIDENCIA_FORM_JSP("")).forward(request, response);
	}
}