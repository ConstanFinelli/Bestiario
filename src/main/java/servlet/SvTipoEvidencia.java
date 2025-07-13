package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import entities.TipoEvidencia;
import logic.LogicTipoEvidencia;

/**
 * Servlet implementation class TipoEvidenciaServlet
 */
@WebServlet("/SvTipoEvidencia")
public class SvTipoEvidencia extends HttpServlet {
	
	public LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvTipoEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		if(id != null) {
			TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), null);
			tipo = controlador.getOne(tipo);
			if(tipo !=null){
			response.getWriter().append("Id: " + tipo.getId() + "<br>Descripción: " +  tipo.getDescripcion());
			}else {
				response.getWriter().append("Tipo de evidencia no encontrada");
			}
		}else {
			LinkedList<TipoEvidencia> tipos = new LinkedList<>();
			tipos = controlador.findAll();
			for(TipoEvidencia tipo : tipos) {
				response.getWriter().append("Id: " + tipo.getId() + "<br>Descripción: " +  tipo.getDescripcion() + "<br>");
			}
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String desc = request.getParameter("descripcion");
		if(desc != null) {
			TipoEvidencia tipo = new TipoEvidencia(0, null);
			tipo.setDescripcion(desc);
			tipo = controlador.save(tipo);
			response.getWriter().append("Id: " + tipo.getId() + "<br>Descripción: " +  tipo.getDescripcion());
		}else {
			response.getWriter().append("Por favor, ingresar una descripción válida");
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		if(id != null) {
			TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), null);
			tipo = controlador.delete(tipo);
			response.getWriter().append("Id: " + tipo.getId() + "<br>Descripción: " +  tipo.getDescripcion());
		}else {
			response.getWriter().append("Tipo de evidencia no encontrado");
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String desc = request.getParameter("descripcion");
		
		if(id != null && desc != null) {
			TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), desc);
			tipo = controlador.update(tipo);
			response.getWriter().append("Id: " + tipo.getId() + "<br>Descripción: " +  tipo.getDescripcion());
		}else {
			response.getWriter().append("Tipo de evidencia no encontrado");
		}
	}

}
