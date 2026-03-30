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

import entities.Bestia;
import entities.Categoria;
import entities.Habitat;
import entities.Registro;
import helpers.CloudinaryHelper;
import helpers.EnvHelper;

@WebServlet("/bestias/editar")
public class EditarBestia extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private LogicCategoria controladorCategoria = new LogicCategoria();
	private LogicHabitat controladorHabitat = new LogicHabitat();
       
    public EditarBestia() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		Bestia bestia = new Bestia(Integer.parseInt(id), null, null, null);
		LinkedList<Habitat> habitats = controladorHabitat.findAll();
		LinkedList<Categoria> categorias = controladorCategoria.findAll();
		RequestDispatcher rd = request.getRequestDispatcher("editarBestia.jsp");
		HttpSession session = request.getSession();
		
		bestia = controlador.getOne(bestia);
		
		Registro registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		
		bestia.setPictureUrl(CloudinaryHelper.getImagenListadoBestia(registro != null ? registro.getMainPic() : EnvHelper.get("DEFAULT_PICTURE_ID")));
		
		session.setAttribute("categorias", categorias);
		session.setAttribute("habitats", habitats);
		session.setAttribute("bestia", bestia);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String peligrosidad = request.getParameter("peligrosidad");
		String estado = "aprobado";
		String action = request.getParameter("action");
		Bestia bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad, estado);
		RequestDispatcher rd = request.getRequestDispatcher("editarBestia.jsp");
		HttpSession session = request.getSession();
		
		if("info".equals(action)) {
			bestia = controlador.update(bestia);
		}else {
			if("category".equals(action)) {
				doCategoryRelation(request, response);
			}else {
				doHabitatRelation(request,response);
			}
			bestia = controlador.getOne(bestia);
		}
		
		Registro registro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		bestia.setPictureUrl(CloudinaryHelper.getImagenListadoBestia(registro != null ? registro.getMainPic() : EnvHelper.get("DEFAULT_PICTURE_ID")));
		
		session.setAttribute("bestia", bestia);
		rd.forward(request, response);
	}
	
	protected void doCategoryRelation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String relationChange = request.getParameter("relationChange");
		String idCategoria = request.getParameter("idCategoria");
		String id = request.getParameter("id");
		Categoria cat = new Categoria(Integer.parseInt(idCategoria), null, null);
		cat = controladorCategoria.getOne(cat);
		Bestia bestia = new Bestia(Integer.parseInt(id),null,null,null);
		bestia = controlador.getOne(bestia);
		
		if("removeCategory".equals(relationChange)) {
			controlador.removeRelation(bestia, cat);
		}else {
			bestia.getCategorias().add(cat);
			controlador.saveCategorias(bestia);
		}
	}
	
	protected void doHabitatRelation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String relationChange = request.getParameter("relationChange");
		String idHabitat = request.getParameter("idHabitat"); 
		String id = request.getParameter("id");
		Habitat hab = new Habitat(Integer.parseInt(idHabitat), null, null, null);
		hab = controladorHabitat.getOne(hab);
		Bestia bestia = new Bestia(Integer.parseInt(id),null,null,null);
		bestia = controlador.getOne(bestia);
		
		if("removeHabitat".equals(relationChange)) {
			controlador.removeRelation(bestia, hab);
		}else {
			bestia.getHabitats().add(hab);
			controlador.saveHabitats(bestia);
		}
	}
}
