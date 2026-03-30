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
 * Servlet implementation class EliminarTipoEvidencia
 */
@WebServlet("/evidencias/eliminarTipoEvidencia")
public class EliminarTipoEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarTipoEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		
		TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), null);
		tipo = controlador.delete(tipo);
		
		String feedbackMessage = "";
		if(tipo == null) {
			feedbackMessage = "¡No se ha podido eliminar el tipo de evidencia!";
		}else {
			feedbackMessage = "¡Tipo de evidencia eliminada con éxito!";
		}
		
		request.setAttribute("feedbackMessage", feedbackMessage);
		request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=tiposEvidencia").forward(request, response);
	}

}
