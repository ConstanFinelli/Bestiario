package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import entities.Categoria;
import logic.LogicCategoria;

/**
 * Servlet implementation class SvCategoria
 */
@WebServlet("/SvCategoria")
public class SvCategoria extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public LogicCategoria controlador = new LogicCategoria();
    /**
     * Default constructor. 
     */
    public SvCategoria() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		String msj = "";
		RequestDispatcher rd = request.getRequestDispatcher("categoriaForms.jsp");
		if(id != null) {
			Categoria cat = new Categoria(Integer.parseInt(id),null, null);
			Categoria categoria = controlador.getOne(cat);
			if(categoria != null) {
				msj = categoria.toString() + "<br><br>";
			}else {
				msj = "Categoria no encontrada";
			}
			request.setAttribute("msjOne", msj);
		}else {
			LinkedList<Categoria> categorias = new LinkedList<>();
			categorias = controlador.findAll();
			for(Categoria cat : categorias) {
				msj = msj + cat + "<br><br>";
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
		String name = request.getParameter("nombre");
		String desc = request.getParameter("descripcion");
		String flag = request.getParameter("flag");
		String msj = "";
		RequestDispatcher rd = request.getRequestDispatcher("categoriaForms.jsp");
		if("create".equals(flag)) {
			if(desc != null || "".equals(desc)) {
				Categoria cat = new Categoria(0, name, desc);
				cat = controlador.save(cat);
				msj = "Categoria Guardada: <br><br>" + cat.toString() + "<br><br>"; 
			}else {
				msj = "Ingrese una descripcion valida"; 
			} request.setAttribute("msjSave", msj);
		}else if("delete".equals(flag)){
			doDelete(request, response);
		}else if("update".equals(flag)) {
			doPut(request, response);
		}
		rd.forward(request, response);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String msj = "";
		if(id != null) {
			Categoria cat = new Categoria(Integer.parseInt(id),null, null);
			cat = controlador.delete(cat);
			msj = "Categoria eliminada: <br><br>" + cat.toString() + "<br><br>";
		}else {
			msj = "Ingrese una id valida";
		}
		request.setAttribute("msjDelete", msj);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String name = request.getParameter("nombre");
		String desc = request.getParameter("descripcion");
		String msj = "";
		Categoria cat = new Categoria(Integer.parseInt(id), name, desc);
		cat = controlador.update(cat);
		if(cat != null) {
			msj = "Categoria Actualizada: " + cat.toString();	
		} else {
			msj = "Categoria no encontrada";
		}
		request.setAttribute("msjUpdate", msj);
	}
}
