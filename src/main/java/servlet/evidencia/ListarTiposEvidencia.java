package servlet.evidencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.TipoEvidencia;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ListarTiposEvidencia
 */
@WebServlet("/evidencias/listarTiposEvidencia")
public class ListarTiposEvidencia extends HttpServlet {
	private LogicTipoEvidencia controlador = new LogicTipoEvidencia();
	private static final Logger logger = Logger.getLogger(ListarTiposEvidencia.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListarTiposEvidencia() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LinkedList<TipoEvidencia> tipos = new LinkedList<>();
		List<String> errores = new ArrayList<>();
		try {
			tipos = controlador.findAll();	
			request.setAttribute("foundTipos", tipos);
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error listando los tipos de evidencia en el servlet ListarTiposEvidencia", e);
			errores.add("No se ha podido listar los tipos de evidencia");
		}
		if(!errores.isEmpty()) {
			errores.add("");
			request.setAttribute("errorGlobal", errores);
		}
		request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=tiposEvidencia").forward(request, response);
	}
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
