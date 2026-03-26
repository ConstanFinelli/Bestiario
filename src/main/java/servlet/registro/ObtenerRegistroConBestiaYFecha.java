package servlet.registro;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import entities.Bestia;
import entities.Registro;
import helpers.CloudinaryHelper;
import helpers.EnvHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ObtenerBestia
 */
@WebServlet("/registros/obtenerRegistroConBestiaYFecha")
public class ObtenerRegistroConBestiaYFecha extends HttpServlet {
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	
	private static final long serialVersionUID = 1L;
     
    public ObtenerRegistroConBestiaYFecha() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.REGISTRO_JSP(""));
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id));
		String fecha = request.getParameter("fecha");
		bestia = controlador.getOne(bestia	);
		Registro registro = null;
		
		if(fecha != null) {
			LocalDateTime fechaParseada = LocalDate.parse(fecha).atStartOfDay();
			registro = controladorRegistro.getRegistroToShow(bestia, fechaParseada);
		}else {
			registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		}
		String UrlImagen = CloudinaryHelper.getImagenRegistro(registro != null ? registro.getMainPic(): EnvHelper.get("DEFAULT_PICTURE_ID"));
		request.setAttribute("UrlImagen", UrlImagen);
		request.setAttribute("foundBestia", bestia);
		request.setAttribute("foundRegistro", registro);
		rd.forward(request, response);
	}

}
