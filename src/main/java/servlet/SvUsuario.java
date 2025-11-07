package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicUsuario;

import java.io.IOException;
import java.util.LinkedList;

import entities.Investigador;
import entities.Lector;
import entities.Usuario;

/**
 * Servlet implementation class SvUsuario
 */
@WebServlet("/SvUsuario")
public class SvUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String SOLICITUDES_INVESTIGADOR_JSP = "solicitudesInvestigador.jsp";
    private LogicUsuario controladorUsuario = new LogicUsuario();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(SOLICITUDES_INVESTIGADOR_JSP);
		String action = request.getParameter("action");
		if("findAllSolicitantes".equals(action)) {
			doFindAll(request, response);
		}
		rd.forward(request, response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if("aprobarSolicitud".equals(action)) {
			doAprobarSolicitud(request, response);
		} else if("rechazarSolicitud".equals(action)) {
			doRechazarSolicitud(request, response);
		} else if("crearSolicitud".equals(action)) {
			doCrearSolcitud(request, response);
		}
	}
	
	protected void doAprobarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String idUsuario = request.getParameter("idUsuario");
		try {
			Usuario user = new Usuario(Integer.parseInt(idUsuario));
			user = (Investigador) controladorUsuario.getOne(user);	
			user.setEstado("investigador");
			controladorUsuario.update((Investigador) user);
		} catch(NumberFormatException e) {
			e.getMessage();
		}
		response.sendRedirect(SOLICITUDES_INVESTIGADOR_JSP);
	}
	
	protected void doRechazarSolicitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String idUsuario = request.getParameter("idUsuario");
		try {
			Investigador user = new Investigador(Integer.parseInt(idUsuario));
			user = (Investigador) controladorUsuario.getOne(user);	
			user.setDni(null);
			user.setApellido(null);
			user.setNombre(null);
			user.setEstado("lector");
			controladorUsuario.update(user);
		} catch(NumberFormatException e) {
			e.getMessage();
		}
		response.sendRedirect(SOLICITUDES_INVESTIGADOR_JSP);
	}
	

	protected void doFindAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		LinkedList<Investigador> solicitantes = controladorUsuario.findAllSolicitantes();
		if(solicitantes.size() != 0) {
			request.setAttribute("solicitantes", solicitantes);
		} else {
			request.setAttribute("errorMsg", "No hay candidaturas en este momento");
		}
		
	}
	
	protected void doCrearSolcitud(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String idUsuario = request.getParameter("idUsuario");
		String nombre = request.getParameter("nombre");
		String apellido = request.getParameter("apellido");
		String dni = request.getParameter("dni");
		Lector user = (Lector) controladorUsuario.getOne(new Usuario(Integer.parseInt(idUsuario)));
		Investigador solicitud = new Investigador (user.getIdUsuario(), user.getCorreo(), user.getContraseña(), nombre, apellido, dni, "solicitante");
		controladorUsuario.update(solicitud);
		response.sendRedirect("home.jsp");
	}
}
