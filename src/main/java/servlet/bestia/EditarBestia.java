package servlet.bestia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import logic.LogicBestia;
import logic.LogicCategoria;
import logic.LogicHabitat;
import logic.LogicRegistro;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Bestia;
import entities.Categoria;
import entities.Habitat;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;

@WebServlet("/bestias/editar")
public class EditarBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private LogicCategoria controladorCategoria = new LogicCategoria();
	private LogicHabitat controladorHabitat = new LogicHabitat();
	private static final Logger logger = Logger.getLogger(EditarBestia.class.getName());
       
    public EditarBestia() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String imagen = null;
		Bestia bestia = null;
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP(""));
		HttpSession session = request.getSession();
		try {
			bestia = new Bestia(Integer.parseInt(id), null, null, null);
		}catch(NumberFormatException nfe) {
			logger.log(Level.WARNING, "Error parseando la id de la bestia en el servlet ActualizarBestia");
			request.setAttribute("errorGlobal","Id invalida");
			rd.forward(request, response);
			return;
		}
		
		LinkedList<Habitat> habitats = null;
		LinkedList<Categoria> categorias = null;
		
		try {
			habitats = controladorHabitat.findAll();
			categorias = controladorCategoria.findAll();
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir categorías y habitats en el servlet EditarBestia", e);
			request.setAttribute("errorGlobal","No se han podido conseguir las habitats y categorías");
			rd.forward(request, response);
			return;
		}
		
		
		try {
			bestia = controlador.getOne(bestia);
			if(bestia == null) {
			request.setAttribute("errorGlobal", "La bestia no ha sido encontrada");
			rd.forward(request, response);
			return;
			}
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al listar bestia editada en el servlet EditarBestia", e);
			request.setAttribute("errorGlobal","Ha habido un error al listar la bestia");
			rd.forward(request, response);
			return;
		}
		
		try {
			imagen = CloudinaryHelper.getImagenListadoBestia(controladorRegistro.getImagen(bestia, LocalDateTime.now()));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir imagen de la bestia editada el servlet EditarBestia", e);
			request.setAttribute("errorGlobal","No se ha podido conseguir la imagen de la bestia");
			rd.forward(request, response);
			return;
		}
		session.setAttribute("categorias", categorias);
		session.setAttribute("habitats", habitats);
		session.setAttribute("bestia", bestia);
		session.setAttribute("imagen", imagen);
		rd.forward(request, response);
	}
	
}
