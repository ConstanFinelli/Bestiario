package servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;

import entities.Bestia;
import entities.Comentario;
import entities.ContenidoRegistro;
import entities.Evidencia;
import entities.Registro;
import entities.TipoEvidencia;
import entities.Usuario;
import logic.LogicBestia;
import logic.LogicComentario;
import logic.LogicContenidoRegistro;
import logic.LogicEvidencia;
import logic.LogicRegistro;
import logic.LogicTipoEvidencia;
import logic.LogicUsuario;

/**
 * Servlet implementation class SbBestia
 */
@WebServlet("/SvBestia")
public class SvBestia extends HttpServlet {
	
	//CONSTANTES
	private final String BESTIA_NOT_FOUND = "No existe una bestia con ese id";
	private final String BESTIAS_NOT_CREATED = "No existen bestias creadas actualmente";
	private final String CREATE_BESTIA_ERROR = "Error al crear la nueva bestia. Revisar datos enviados";
	private final String REGISTRO_NOT_FOUND = "No existe un registro asociado";
	private final String ID_FORMAT_ERROR = "Error en el formato del id ingresado";
	private final String BESTIA_FORMS_JSP = "bestiaForms.jsp";
	private final String BESTIA_LIST_JSP = "bestias.jsp";
	private final String REGISTRO_JSP = "registro.jsp";
	private final String ACTUALIZACION_REGISTRO_JSP = "nuevoRegistro.jsp";
	private LogicBestia controlador = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private LogicComentario controladorComentario = new LogicComentario();
	private LogicUsuario controladorUsuario = new LogicUsuario();
	private LogicContenidoRegistro controladorCr = new LogicContenidoRegistro();
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	//##GET ONE##
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String fechaParam = request.getParameter("fecha");
		
		RequestDispatcher rd = null;
		
		Registro registro = null;
		LocalDate fecha = null;
		
		if (fechaParam != null && !fechaParam.isEmpty()) {
		    fecha = LocalDate.parse(fechaParam);
		} else{
			fecha = LocalDate.now();
		};
		
		String action = request.getParameter("action");
		
		if("form".equals(action)) {
			rd = request.getRequestDispatcher(BESTIA_FORMS_JSP);
		}else if("list".equals(action)){
			rd = request.getRequestDispatcher(BESTIA_LIST_JSP);
		}else if("registro".equals(action)) {
			rd = request.getRequestDispatcher(REGISTRO_JSP);
		}else if("actualizacion".equals(action)) {
			rd = request.getRequestDispatcher(ACTUALIZACION_REGISTRO_JSP);
			LinkedList<TipoEvidencia> tes = controladorTipoEvidencia.findAll();
			request.setAttribute("tes", tes);
		}else {
			response.sendRedirect("home.jsp");
			return;
		}
		String getOneMsg = "";
		String findAllMsg = "";
		if(id != null) {
			Bestia bestia = new Bestia(Integer.parseInt(id));
			bestia = controlador.getOne(bestia);
			if(bestia != null) {
				getOneMsg = getOneMsg + bestia + "<br><br>";
				registro = controladorRegistro.getRegistroToShow(bestia, fecha);
				if (registro == null) {
                    request.getSession().setAttribute("errorMsg", 
                        REGISTRO_NOT_FOUND);
                    response.sendRedirect("SvBestia?action=list");
                    return; 
                }
			}else {
				getOneMsg = BESTIA_NOT_FOUND;
				
			}	
			request.setAttribute("getOneMsg", getOneMsg);
			request.setAttribute("bestia", bestia);
			request.setAttribute("registro", registro);
		} else {
			LinkedList<Bestia> bestias = new LinkedList<>();
			
			String filter = request.getParameter("filter"); // filtro por categoria 
			if(filter != null && !filter.isEmpty()) {
				bestias= controlador.findByCategoria(filter);
				
			} else {
				bestias = controlador.findAll();
			}
			
			if (!bestias.isEmpty()) {
				request.setAttribute("bestias", bestias);
				for (Bestia unabestia : bestias) {
					findAllMsg = findAllMsg + unabestia + "<br><br>";
				} 
			} else {
				findAllMsg = BESTIAS_NOT_CREATED;
			}
			if(filter != null) {
				request.setAttribute("searchedFilter", filter); //Para que siga estando el filtro en el buscar 
			}
			
			request.setAttribute("findAllMsg", findAllMsg);
		}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String nombre = request.getParameter("nombre");
		String peligrosidad= request.getParameter("peligrosidad");
		String flag = request.getParameter("flag");
		String saveMsg = "";
		RequestDispatcher rd = request.getRequestDispatcher(BESTIA_FORMS_JSP); 
		
		String action = request.getParameter("action");
		
		if("actualizacion".equals(action)) {
			rd = request.getRequestDispatcher(ACTUALIZACION_REGISTRO_JSP);
			String introduccion = request.getParameter("introduccion");
			String resumen = request.getParameter("resumen");
			String historia = request.getParameter("historia");
			String descripcion = request.getParameter("descripcion");
			String bestiaId = request.getParameter("bestia");
			Bestia bestia = controlador.getOne(new Bestia(Integer.parseInt(bestiaId),null,null));
			
			String[] fechas = request.getParameterValues("fechaObtencion");
			String[] tipos = request.getParameterValues("tipo");
			String[] links = request.getParameterValues("link");

			for (int i = 0; i < fechas.length; i++) {
			    LocalDate fecha = LocalDate.parse(fechas[i]);
			    String tipo = tipos[i];
			    String link = links[i];
			    TipoEvidencia te = new TipoEvidencia(Integer.parseInt(tipo), null);
			    te = controladorTipoEvidencia.getOne(te);
			    Evidencia evidencia = new Evidencia(0,fecha,link,"inactivo",te);
			    controladorEvidencia.save(evidencia);
			}
			
			if(introduccion != null && resumen != null && historia != null && descripcion != null) {
				ContenidoRegistro contenido = new ContenidoRegistro(0, introduccion, historia,descripcion, resumen);
				contenido = controladorCr.save(contenido);
				Registro registro = new Registro(0,contenido, null, null, null, "pendiente", bestia );
				controladorRegistro.save(registro);
				response.sendRedirect("SvBestia?action=registro&id="+bestiaId);
				return;
			}
		}else {
		if (flag.equals("post")) {
			try {

				if (nombre != null && peligrosidad != null) {
					Bestia bestia = new Bestia(nombre, peligrosidad);
					bestia = controlador.save(bestia);
					saveMsg = saveMsg + bestia + "<br><nr>";
				} else {
					saveMsg = CREATE_BESTIA_ERROR;
				}
			} catch (NumberFormatException e) {
				e.getMessage();
				saveMsg = ID_FORMAT_ERROR;
			} finally {
				request.setAttribute("saveMsg", saveMsg);
			} 
		} else if(flag.equals("put")) {
			doPut(request, response);
		} else if(flag.equals("delete")){
			doDelete(request,response);
		} else {
			String contenido = request.getParameter("contenido");
			String idUsuario = request.getParameter("idUsuario");
			String idBestia = request.getParameter("idBestia");
			if(contenido != null && idUsuario != null && idBestia != null) {
				LocalDate fechaComentario = LocalDate.now();
				Usuario usuario = controladorUsuario.getOne(new Usuario(Integer.parseInt(idUsuario), null, null));
				Bestia bestia = controlador.getOne(new Bestia(Integer.parseInt(idBestia),null,null));
				Comentario comentario = new Comentario(usuario, bestia, fechaComentario, contenido);
				controladorComentario.save(comentario);
			}
			rd = request.getRequestDispatcher(REGISTRO_JSP);
			String redirectUrl = request.getContextPath() + "/SvBestia?action=registro&id=" + idBestia;
			 response.sendRedirect(redirectUrl);
			 return;
		}}
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String updateMsg = "";
		String id = request.getParameter("id");
		String nombre = request.getParameter("nombre");
		String peligrosidad = request.getParameter("peligrosidad");
		
		try {
			Bestia bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad);
			bestia = controlador.update(bestia);
			if(bestia != null) { 
				updateMsg = updateMsg + bestia + "<br><br>";
			}else{
				updateMsg = BESTIA_NOT_FOUND;
			}

			
			
		} catch (NumberFormatException e) {
			
			e.getMessage();
			updateMsg = ID_FORMAT_ERROR;
		} finally {
			
			request.setAttribute("updateMsg", updateMsg);
		}
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		String msgDelete = "";
		Bestia bestia = new Bestia(Integer.parseInt(id));
		bestia = controlador.delete(bestia);
		if(bestia == null) {
			msgDelete = BESTIA_NOT_FOUND;
		} else {
			msgDelete = "Eliminado: " + bestia + "<br><br>";
		}
		request.setAttribute("deleteMsg", msgDelete);

	}

}