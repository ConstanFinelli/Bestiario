package servlet.evidencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEvidencia;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.time.LocalDateTime;

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class CrearEvidencia
 */
@WebServlet("/evidencias/crear")
public class CrearEvidencia extends HttpServlet {
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia(); 
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrearEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String link = request.getParameter("link");
		String estado = request.getParameter("estado");
		String idTipoEvidencia = request.getParameter("idTipoEvidencia");
		TipoEvidencia tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(idTipoEvidencia), null)); 
		LocalDateTime fechaO = LocalDateTime.parse(request.getParameter("fechaObtencion"));
		Evidencia evidencia = controladorEvidencia.save(new Evidencia(0, fechaO, estado, link, tipo)); 
		request.setAttribute("createdEvidencia", evidencia);
		request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP("")).forward(request, response);
	}

}
