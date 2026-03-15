package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

import logic.LogicRegistro;
import logic.LogicTipoEvidencia;

import entities.Registro;
import entities.TipoEvidencia;
import helpers.HttpRoutes;
import entities.Bestia;
import entities.Investigador;

/**
 * Servlet implementation class SvRegistro
 */
@WebServlet("/SvRegistro")
public class SvRegistro extends HttpServlet {
	
	private LogicRegistro controlador = new LogicRegistro();
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SvRegistro() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RequestDispatcher rd = null;
		String action = request.getParameter("action");
		
		if("actualizacion".equals(action)) {
			rd = request.getRequestDispatcher(HttpRoutes.REGISTRO_JSP(request.getContextPath()));
			LinkedList<TipoEvidencia> tes = controladorTipoEvidencia.findAll();
			request.setAttribute("tes", tes);
		}
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		int nroRegistro = Integer.parseInt(request.getParameter("nroRegistro"));
		int idBestia = Integer.parseInt(request.getParameter("idBestia"));
		Registro registro = new Registro(nroRegistro, new Bestia(idBestia));
		
		if (action.equals("aceptar")) {
			int idInvestigador = Integer.parseInt(request.getParameter("idInvestigador"));
			registro.setPublicador(new Investigador(idInvestigador));
			controlador.updateEstado(registro);
		} else if (action.equals("rechazar")) {
			controlador.delete(registro);
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTROS_PENDIENTES_JSP(request.getContextPath()));
		rd.forward(request, response);
		
	}

}
