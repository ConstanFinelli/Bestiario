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
 * Servlet implementation class ActualizarTipoEvidencia
 */
@WebServlet("/evidencias/actualizarTipoEvidencia")
public class ActualizarTipoEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	private static final long serialVersionUID = 1L;
       

    public ActualizarTipoEvidencia() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String desc = request.getParameter("descripcion");
		TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), desc);
		tipo = controlador.update(tipo);
		request.setAttribute("updatedTipo", tipo);
		request.getRequestDispatcher(HttpRoutes.TIPO_EVIDENCIA_FORM_JSP("")).forward(request, response);
	}

}
