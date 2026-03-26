package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import logic.LogicBestia;
import logic.LogicRegistro;

import java.io.IOException;
import java.time.LocalDateTime;

import entities.Bestia;
import entities.Registro;
import helpers.CloudinaryHelper;
import helpers.EnvHelper;

@WebServlet("/bestias/editarBestia")
public class EditarBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
       
    public EditarBestia() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id), null, null, null);
		RequestDispatcher rd = request.getRequestDispatcher("editarBestia.jsp");
		
		bestia = controlador.getOne(bestia);
		
		Registro registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		
		bestia.setPictureUrl(CloudinaryHelper.getImagenListadoBestia(registro != null ? registro.getMainPic() : EnvHelper.get("DEFAULT_PICTURE_ID")));
		
		request.setAttribute("bestia", bestia);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String peligrosidad = request.getParameter("peligrosidad");
		String estado = request.getParameter("estado");
		Bestia bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad, estado);
		RequestDispatcher rd = request.getRequestDispatcher("editarBestia.jsp");
		
		bestia = controlador.update(bestia);

		rd.forward(request, response);
	}

}
