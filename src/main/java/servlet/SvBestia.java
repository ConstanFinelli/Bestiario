package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

import entities.Bestia;
import logic.LogicBestia;

/**
 * Servlet implementation class SbBestia
 */
@WebServlet("/SvBestia")
public class SvBestia extends HttpServlet {
	
	//CONSTANTES
	private final String BESTIA_NOT_FOUND = "No existe una bestia con ese id";
	private final String BESTIAS_NOT_CREATED = "No existen bestias creadas actualmente";
	private final String CREATE_BESTIA_ERROR = "Error al crear la nueva bestia. Revisar datos enviados";
	private final String ID_FORMAT_ERROR = "Error en el formato del id ingresado";
	private LogicBestia controlador = new LogicBestia();
	
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	//##GET ONE##
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		RequestDispatcher rd = request.getRequestDispatcher("bestiaForms.jsp");
		String getOneMsg = "";
		String findAllMsg = "";
		if(id != null) {
			Bestia bestia = new Bestia(Integer.parseInt(id));
			bestia = controlador.getOne(bestia);
			if(bestia != null) {
				getOneMsg = getOneMsg + bestia + "<br><br>";
				
			}else {
				getOneMsg = BESTIA_NOT_FOUND;
				
			}	
			request.setAttribute("getOneMsg", getOneMsg);
		} else {
			LinkedList<Bestia> bestias = new LinkedList<>();
			bestias = controlador.findAll();
			if (!bestias.isEmpty()) {
				for (Bestia unabestia : bestias) {
					findAllMsg = findAllMsg + unabestia + "<br><br>";
				} 
			} else {
				findAllMsg = BESTIAS_NOT_CREATED;
			}
			request.setAttribute("findAllMsg", findAllMsg);
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String nombre = request.getParameter("nombre");
		String peligrosidad= request.getParameter("peligrosidad");
		String flag = request.getParameter("flag");
		String saveMsg = "";
		RequestDispatcher rd = request.getRequestDispatcher("bestiaForms.jsp"); 
		
		if (flag.equals("post")) {
			try {

				if (nombre != null && peligrosidad != null) {
					Bestia bestia = new Bestia(nombre, peligrosidad);
					bestia = controlador.save(bestia);
					saveMsg = saveMsg + bestia + "<br><nr>";
				} else {
					saveMsg = CREATE_BESTIA_ERROR;
				}
			} catch (NumberFormatException e) {
				e.getMessage();
				saveMsg = ID_FORMAT_ERROR;
			} finally {
				request.setAttribute("saveMsg", saveMsg);
			} 
		} else if(flag.equals("put")) {
			doPut(request, response);
		} else {
			doDelete(request,response);
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("bestiaForms.jsp");
		String updateMsg = "";
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String peligrosidad = request.getParameter("peligrosidad");
		try {
			Bestia bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad);
			bestia = controlador.update(bestia);
			if(bestia != null) { 
				updateMsg = updateMsg + bestia + "<br><br>";
			}else{
				updateMsg = BESTIA_NOT_FOUND;
			}

			
			
		} catch (NumberFormatException e) {
			
			e.getMessage();
			updateMsg = ID_FORMAT_ERROR;
		} finally {
			
			request.setAttribute("updateMsg", updateMsg);
			rd.forward(request, response);
		}
		;
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		RequestDispatcher rd = request.getRequestDispatcher("bestiaForms.jsp");
		String msgDelete = "";
		Bestia bestia = new Bestia(Integer.parseInt(id));
		bestia = controlador.delete(bestia);
		if(bestia == null) {
			msgDelete = BESTIA_NOT_FOUND;
		} else {
			msgDelete = "Eliminado: " + bestia + "<br><br>";
		}
		request.setAttribute("deleteMsg", msgDelete);
		rd.forward(request, response);

	}

}
