package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicHabitat;

import java.io.IOException;
import java.util.LinkedList;

import entities.Habitat;

/**
 * Servlet implementation class SvHabitat
 */
@WebServlet("/SvHabitat")
public class SvHabitat extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private LogicHabitat controlador = new LogicHabitat();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		RequestDispatcher rd = request.getRequestDispatcher("habitatForms.jsp");
		String getOneMsg = "";
		String findAllMsg = "";
		if(id != null) {
			Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
			ht = controlador.getOne(ht);
			if(ht !=null){
				getOneMsg = getOneMsg + ht + "<br><br>";
				request.setAttribute("getOneMsg", getOneMsg);
			}else {
				getOneMsg = "Hábitat no encontrada";
				request.setAttribute("getOneMsg", getOneMsg);
			}
		}else {
			LinkedList<Habitat> hts = new LinkedList<>();
			hts = controlador.findAll();
			for(Habitat ht : hts) {
				findAllMsg = findAllMsg + ht + "<br><br>";
			}
			request.setAttribute("findAllMsg", findAllMsg);
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		String localizacion = request.getParameter("localizacion");
		String id = request.getParameter("id");
		String delete = request.getParameter("delete");
		RequestDispatcher rd = request.getRequestDispatcher("habitatForms.jsp");
		String saveMsg = "";
		if(nombre != null && localizacion != null && delete == null) {
			if(id == null) {
			Habitat ht = new Habitat(0, nombre, null, localizacion);
			ht = controlador.save(ht);
			saveMsg = saveMsg + ht + "<br><br>";
			request.setAttribute("saveMsg", saveMsg);
			}else {
				doPut(request, response);	
			}
		}else {
			if(delete == null) {
				saveMsg = "Ingresar descripcion o localizacion";
				request.setAttribute("saveMsg", saveMsg);
			}else {
				doDelete(request, response);
			}
		}
		rd.forward(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		RequestDispatcher rd = request.getRequestDispatcher("habitatForms.jsp");
		String deleteMsg = "";
		if(id != null) {
			Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
			ht = controlador.delete(ht);
			deleteMsg = deleteMsg + ht + "<br><br>";
			request.setAttribute("deleteMsg", deleteMsg);
		}else {
			deleteMsg = "Hábitat no encontrada";
			request.setAttribute("deleteMsg", deleteMsg);
		}
		rd.forward(request, response);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String localizacion = request.getParameter("localizacion");
		String updateMsg = "";
		if(id != null && nombre != null && localizacion != null) {
			Habitat ht = new Habitat(Integer.parseInt(id), nombre, null, localizacion);
			ht = controlador.update(ht);
			updateMsg = updateMsg + ht + "<br><br>";
			request.setAttribute("updateMsg", updateMsg);
		}else {
			updateMsg = "Hábitat no encontrada";
			request.setAttribute("updateMsg", updateMsg);
		}
	}
	
}
