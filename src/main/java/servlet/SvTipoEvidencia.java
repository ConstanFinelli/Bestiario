package servlet;

import jakarta.servlet.RequestDispatcher;
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
		RequestDispatcher rd = request.getRequestDispatcher("tipoEvidenciaForms.jsp");
		String msj = "";
		if(id != null) {
			TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), null);
			tipo = controlador.getOne(tipo);
			if(tipo !=null){
			msj = msj + tipo.toString();
			}else {
				msj = "Tipo de evidencia no encontrada"; 
			}
			request.setAttribute("msjOne", msj);
		}else {
			LinkedList<TipoEvidencia> tipos = new LinkedList<>();
			tipos = controlador.findAll();
			for(TipoEvidencia tipo : tipos) {
				msj = msj + "<br>" + tipo.toString();
			}
			request.setAttribute("msjAll", msj);
		}
		rd.forward(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String desc = request.getParameter("descripcion");
		String msj = "";
		String flag = request.getParameter("flag");
		RequestDispatcher rd = request.getRequestDispatcher("tipoEvidenciaForms.jsp");
		if("create".equals(flag)) {
			if(desc != null) {
				TipoEvidencia tipo = new TipoEvidencia(0, null);
				tipo.setDescripcion(desc);
				tipo = controlador.save(tipo);
				msj = "Categoria Agregada: <br><br>" + tipo.toString();
			}else {
				msj = "Por favor, ingresar una descripción válida";
			}
			request.setAttribute("msjCreate", msj);
		} else if("update".equals(flag)) {
			doPut(request, response);
		} else if("delete".equals(flag)) {
			doDelete(request, response);
		}
		rd.forward(request, response);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String msj = "";
		if(id != null) {
			TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), null);
			tipo = controlador.delete(tipo);
			msj = tipo.toString();
		}else {
			msj = "Tipo de evidencia no encontrado";
		}
		request.setAttribute("msjDelete", msj);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String desc = request.getParameter("descripcion");
		String msj = "";
		TipoEvidencia tipo = new TipoEvidencia(Integer.parseInt(id), desc);
		tipo = controlador.update(tipo);
		if(tipo != null) {	
			msj = "Categoria Actualizada: <br>" + tipo.toString();
		}else {
			msj = "Tipo de evidencia no encontrado";
		}
		request.setAttribute("msjUpdate", msj);
	}

}
