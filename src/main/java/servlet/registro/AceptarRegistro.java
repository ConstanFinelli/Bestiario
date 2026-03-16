package servlet.registro;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicRegistro;

import java.io.IOException;

import entities.Bestia;
import entities.Investigador;
import entities.Registro;
import helpers.HttpRoutes;

/**
 * Servlet implementation class AceptarRegistro
 */
@WebServlet("/registros/aceptarRegistro")
public class AceptarRegistro extends HttpServlet {
	private LogicRegistro controlador = new LogicRegistro();
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AceptarRegistro() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTROS_PENDIENTES_JSP(""));
		rd.forward(request, response);
	}

}
