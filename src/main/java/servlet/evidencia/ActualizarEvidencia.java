package servlet.evidencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import logic.LogicEvidencia;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.time.LocalDateTime;

import entities.Evidencia;
import entities.TipoEvidencia;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarEvidencia
 */
@MultipartConfig
@WebServlet("/evidencias/actualizar")
public class ActualizarEvidencia extends HttpServlet {
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarEvidencia() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TipoEvidencia tipo = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(request.getParameter("idTipo")), null));
		Part archivo = request.getPart("archivo");
		String nroEvidencia = request.getParameter("nroEvidencia");
		String fileId = CloudinaryHelper.upload(archivo);
		Evidencia evidencia = controladorEvidencia.update(new Evidencia(Integer.parseInt(nroEvidencia), LocalDateTime.parse(request.getParameter("fechaObtencion")), request.getParameter("estado"), fileId , tipo));
		request.setAttribute("updatedEvidencia", evidencia);
		request.getRequestDispatcher(HttpRoutes.EVIDENCIA_FORM_JSP("")).forward(request, response);
	}

}
