package servlet.registro;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicRegistro;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

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
	private static final Logger logger = Logger.getLogger(AceptarRegistro.class.getName());
	
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
		int nroRegistro = 0;
		int idBestia = 0;
		try{
			nroRegistro = Integer.parseInt(request.getParameter("nroRegistro"));
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear nroRegistro en el servlet AceptarRegistro", e);
			request.setAttribute("errorGlobal", "El número de registro es inválido.");
			return;
		}
		
		try{
			idBestia = Integer.parseInt(request.getParameter("idBestia"));
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear idBestia en el servlet AceptarRegistro", e);
			request.setAttribute("errorGlobal", "El id de la bestia es inválido.");
			return;
		}
		Registro registro = new Registro(nroRegistro, new Bestia(idBestia));
		if (action.equals("aceptar")) {
			try{
				int idInvestigador = Integer.parseInt(request.getParameter("idInvestigador"));
				registro.setPublicador(new Investigador(idInvestigador));
				controlador.updateEstado(registro);
			} catch(NumberFormatException e) {
				logger.log(Level.WARNING, "Error al parsear idInvestigador en el servlet AceptarRegistro", e);
				request.setAttribute("errorGlobal", "El id del investigador es inválido.");
				return;
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al aceptar registro en el servlet AceptarRegistro", e);
				request.setAttribute("errorGlobal", "No se ha podido aceptar el registro. ");
			}
		} else if (action.equals("rechazar")) {
			try{
				controlador.delete(registro);
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al rechazar registro en el servlet AceptarRegistro", e);
				request.setAttribute("errorGlobal", "No se ha podido rechazar el registro. ");
			}
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTROS_PENDIENTES_JSP(""));
		rd.forward(request, response);
	}

}
