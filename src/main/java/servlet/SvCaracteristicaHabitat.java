package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicCaracteristicaHabitat;

import java.io.IOException;
import java.util.LinkedList;

import entities.CaracteristicaHabitat;
import entities.Habitat;


/**
 * Servlet implementation class SvCaracteristicaHabitat
 */
@WebServlet("/SvCaracteristicaHabitat")
public class SvCaracteristicaHabitat extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private LogicCaracteristicaHabitat controlador = new LogicCaracteristicaHabitat();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvCaracteristicaHabitat() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		RequestDispatcher rd = request.getRequestDispatcher("carHabitatForms.jsp");
		String findAllMsg = "";
		
		if(id != null) {
			Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
			LinkedList<CaracteristicaHabitat> hts = new LinkedList<>();
			hts = controlador.findAllById(ht);
			if(hts.isEmpty()) {
				findAllMsg = "Sin resultados";
		} else {
			findAllMsg = "ID del habitat asociado: " + ht.getId() + "<br>";
			for(CaracteristicaHabitat hti : hts) {
				findAllMsg = findAllMsg + hti + ", ";
			}
		}
		
	}
		request.setAttribute("findAllMsg", findAllMsg);
		
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		String flag = request.getParameter("flag");
		RequestDispatcher rd = request.getRequestDispatcher("carHabitatForms.jsp");
		String saveMsg = "";
		
		if(flag.equals("post")) {
			Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
			CaracteristicaHabitat ch = new CaracteristicaHabitat(ht.getId(), descripcion);
			ch = controlador.save(ch, ht);
			saveMsg = saveMsg + ch + "<br><br>";
			if(ch == null) {
				saveMsg = "Característica no se ha podido crear";
			}
			request.setAttribute("saveMsg", saveMsg);
		}
		else if(flag.equals("put")) {
				doPut(request, response);
		}else {
				doDelete(request, response);
			}
		rd.forward(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		String deleteMsg = "";
		
		CaracteristicaHabitat ch = new CaracteristicaHabitat(Integer.parseInt(id), descripcion);
		ch = controlador.delete(ch);
		deleteMsg = deleteMsg + "Característica " + ch +  "eliminada";
		if(ch == null) {
			deleteMsg = "Característica no encontrada";
		}
		request.setAttribute("deleteMsg", deleteMsg);
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String id = request.getParameter("id");
		String descripcion = request.getParameter("descripcion");
		String newDescripcion = request.getParameter("newDescripcion");
		String updateMsg = "";
		
		CaracteristicaHabitat ch = new CaracteristicaHabitat(Integer.parseInt(id), descripcion);
		ch = controlador.update(ch, newDescripcion);
		if(ch == null) {
			updateMsg = "Característica no encontrada";
		}else {
			updateMsg = updateMsg + "ID del hábitat asociado: " + ch.getIdHabitat() + 
					"<br>Característica nueva: " + newDescripcion;
		}
		request.setAttribute("updateMsg", updateMsg);
	}
}
