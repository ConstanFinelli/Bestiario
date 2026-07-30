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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;

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
		List<String> errores = new ArrayList<>();
		
		Bestia bestia = new Bestia(Integer.parseInt(id), null, null, null);
		LinkedList<Habitat> habitats = null;
		LinkedList<Categoria> categorias = null;
		
		try {
			habitats = controladorHabitat.findAll();
			categorias = controladorCategoria.findAll();
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir categorías y habitats en el servlet EditarBestia", e);
			errores.add("No se han podido conseguir las habitats y categorías");
		}
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.EDITAR_BESTIA_JSP(""));
		HttpSession session = request.getSession();
		
		try {
			bestia = controlador.getOne(bestia);
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al listar bestia editada en el servlet EditarBestia", e);
			errores.add("No se ha podido listar la bestia editada");
		}
		
		try {
			imagen = CloudinaryHelper.getImagenListadoBestia(controladorRegistro.getImagen(bestia, LocalDateTime.now()));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir imagen de la bestia editada el servlet EditarBestia", e);
			errores.add("No se ha podido conseguir la imagen de la bestia");
		}
		
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde.");
			request.setAttribute("errorGlobal", errores);
		}
		
		session.setAttribute("categorias", categorias);
		session.setAttribute("habitats", habitats);
		session.setAttribute("bestia", bestia);
		session.setAttribute("imagen", imagen);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String peligrosidad = request.getParameter("peligrosidad");
		String estado = "aprobado";
		String action = request.getParameter("action");
		Bestia bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad, estado);
		String imagen = "";
		List<String> errores = new ArrayList<>();
		RequestDispatcher rd = request.getRequestDispatcher("editarBestia.jsp");
		HttpSession session = request.getSession();
		
		try {
			if("info".equals(action)) {
				bestia = controlador.update(bestia);
			}else {
				bestia = controlador.getOne(bestia);
			}
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al editar bestia en el servlet EditarBestia", e);
			errores.add("No se han podido editar la bestia seleccionada");
		}
		
		try {
			imagen = CloudinaryHelper.getImagenListadoBestia(controladorRegistro.getImagen(bestia, LocalDateTime.now()));
		}catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir imagen de la bestia a editar el servlet EditarBestia", e);
			errores.add("No se ha podido conseguir la imagen de la bestia");
		}
		
		if(!errores.isEmpty()) {
			errores.add("Por favor, intente mas tarde.");
			request.setAttribute("errorGlobal", errores);
		}
		
		session.setAttribute("imagen", imagen);
		session.setAttribute("bestia", bestia);
		rd.forward(request, response);
	}
	
}
