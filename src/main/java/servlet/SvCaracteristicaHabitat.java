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
		String msj = "";
		RequestDispatcher rd = request.getRequestDispatcher("");
		if(id != null) {
			Habitat ht = new Habitat(Integer.parseInt(id), null, null, null);
			LinkedList<CaracteristicaHabitat> ch = controlador.findAllById(ht);
			if(!ch.isEmpty()){
				for(CaracteristicaHabitat carh : ch) {
					msj = msj + carh + "<br><br>";
				}
			}else {
				msj = "Ese Hábitat no tiene caracteristicas asignadas";
			}
		}else {
				msj = "Por favor, ingresar una ID";
		}
		request.setAttribute("msjGet", msj);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String desc = request.getParameter("descripcion");
		String idHab = request.getParameter("idHabitat");
		if(desc != null && idHab != null) {
			CaracteristicaHabitat ch = new CaracteristicaHabitat(0, desc);
			Habitat ht = new Habitat(Integer.parseInt(idHab), null, null, null);
			ch = controlador.save(ch, ht);
			response.getWriter().append("<div>Característica agregada: " + ch + " al Habitat con id: "+ idHab +"</div>");
		}else {
			response.getWriter().append("Por favor ingresar una descripción y/o ID válidos.");
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String idHab = request.getParameter("id");
		String desc = request.getParameter("descripcion");
		if(desc != null && idHab != null) {
			CaracteristicaHabitat ch = new CaracteristicaHabitat(Integer.parseInt(idHab), null);
			ch = controlador.delete(ch);
			response.getWriter().append("<div>Eliminada Característica: "+ ch +" del Habitat con id: " + idHab + "</div>");
		}else {
			response.getWriter().append("Característica de Hábitat no encontrada");
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String idHab = request.getParameter("id");
		String desc = request.getParameter("descripcion");
		
		if(idHab != null && desc != null) {
			CaracteristicaHabitat ch = new CaracteristicaHabitat(Integer.parseInt(idHab), desc);
			ch = controlador.update(ch);
			response.getWriter().append("<div> Actualizada característica: "+ ch +" del Habitat con id: " + idHab + "</div>");
		}else {
			response.getWriter().append("Característica de Hábitat no encontrada");
		}
	}
	

}
