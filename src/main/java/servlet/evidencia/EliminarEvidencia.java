package servlet.evidencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicEvidencia;

import java.io.IOException;

import entities.Evidencia;
import entities.TipoEvidencia;

/**
 * Servlet implementation class EliminarEvidencia
 */
@WebServlet("/evidencias/eliminar")
public class EliminarEvidencia extends HttpServlet {
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminarEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int idTipo = Integer.parseInt(request.getParameter("idTipoEvidencia"));
		int nroEvidencia = Integer.parseInt(request.getParameter("nroEvidencia"));
		Evidencia evidencia = controladorEvidencia.delete(new Evidencia(nroEvidencia, null, null, null, new TipoEvidencia(idTipo, null)));
		request.setAttribute("deletedEvidencia", evidencia);
	}

}
