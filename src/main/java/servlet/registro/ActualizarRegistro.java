package servlet.registro;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import logic.LogicBestia;
import logic.LogicEvidencia;
import logic.LogicRegistro;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;

import entities.Bestia;
import entities.Evidencia;
import entities.Investigador;
import entities.Registro;
import entities.TipoEvidencia;
import entities.Usuario;
import helpers.CloudinaryHelper;
import helpers.HttpRoutes;

/**
 * Servlet implementation class ActualizarRegistro
 */
@MultipartConfig(
	fileSizeThreshold = 1024 * 1024 * 2,  // 2MB memoria antes de escribir temporal en disco
	maxFileSize = 1024 * 1024 * 50,       // 50MB máximo por archivo individual
	maxRequestSize = 1024 * 1024 * 100    // 100MB máximo por petición total
)
@WebServlet("/registros/actualizarRegistro")
public class ActualizarRegistro extends HttpServlet {
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	private LogicBestia controladorBestia = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private static final Logger logger = Logger.getLogger(ActualizarRegistro.class.getName());
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualizarRegistro() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.ACTUALIZACION_REGISTRO_JSP(""));
		String idBestia = request.getParameter("id");
		Bestia bestia = null;
		LinkedList<TipoEvidencia>  tes = null;
		Registro ultimoRegistro = null;
		try{
			bestia = new Bestia(Integer.parseInt(idBestia));
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear idBestia en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "El id de la bestia es inválido.");
			return;
		}
		try{
			bestia = controladorBestia.getOne(bestia);
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir bestia en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "No se ha conseguido la bestia. ");
			return;
		}
		try{
			tes = controladorTipoEvidencia.findAll();
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir tipos de evidencia en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "No se han conseguido los tipos de evidencia. ");
			return;
		}
		try{
			ultimoRegistro = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir último registro en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "No se ha conseguido el último registro. ");
			return;
		}
		request.setAttribute("tiposEvidencia", tes);
		request.setAttribute("foundBestia", bestia);
		request.setAttribute("foundRegistro", ultimoRegistro);
		
		rd.forward(request, response);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String introduccion = request.getParameter("introduccion");
		String resumen = request.getParameter("resumen");
		String historia = request.getParameter("historia");
		String descripcion = request.getParameter("descripcion");
		String mainPic = null;
		HttpSession session = request.getSession();
		
		Usuario usuario = (Usuario) session.getAttribute("user");	
		String bestiaId = request.getParameter("id");

		Bestia bestia = null;
		Registro registroActual = null;
		
		try{
			bestia = new Bestia(Integer.parseInt(bestiaId));
		}catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear idBestia en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "El id de la bestia es inválido.");
			return;
		}

		Part filePart = request.getPart("mainPic");
		if(filePart != null && filePart.getSize() > 0 ) {
			try{
				mainPic = CloudinaryHelper.upload(filePart);
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al subir imagen de bestia en el servlet ActualizarRegistro", e);
				request.setAttribute("errorGlobal", "No se ha podido subir la imagen de la bestia. ");
				return;
			}
		}else {
			try{
				mainPic = controladorRegistro.getImagen(new Bestia(Integer.parseInt(bestiaId)), LocalDateTime.now());
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al obtener imagen en el servlet ActualizarRegistro", e);
				request.setAttribute("errorGlobal", "No se ha podido obtener la imagen de la bestia. ");
				return;
			}
		}
		
		
		
		try{
			registroActual = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		}catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir registro actual en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "No se ha conseguido el registro actual. ");
			return;
		}
		if(registroActual != null) {
			if(descripcion.equals(registroActual.getDescripcion()) && historia.equals(registroActual.getHistoria()) 
					&& introduccion.equals(registroActual.getIntroduccion()) && resumen.equals(registroActual.getResumen()))
			{ // verificar si no hubo modificaciones
				descripcion = null;
				introduccion = null;
				historia = null;
				resumen = null;
			}
		}
		try{
			bestia = controladorBestia.getOne(new Bestia(Integer.parseInt(bestiaId),null,null, null));
		}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al conseguir bestia en el servlet ActualizarRegistro", e);
				request.setAttribute("errorGlobal", "No se ha conseguido la bestia. ");
				return;
			}
		
		String[] fechas = request.getParameterValues("fechaObtencion");
		String[] tipos = request.getParameterValues("tipo");
		Collection<Part> parts = request.getParts();
		LinkedList<String> archivos = new LinkedList<>();
		for(Part p: parts) {
			if(p.getName().equals("archivo") && p.getSize() > 0) {
				try{
					String id = CloudinaryHelper.upload(p);
					archivos.add(id);
				}catch(Exception e) {
					logger.log(Level.SEVERE, "Error al subir archivo de evidencia en el servlet ActualizarRegistro", e);
					request.setAttribute("errorGlobal", "No se ha podido subir los archivos de evidencias. ");
					return;
				}
			}
		}
		
		if(fechas != null) {
			LinkedList<Evidencia> evidencias = new LinkedList<>();
			LocalDate fechaSinHora = null;
			TipoEvidencia te = null;
			for (int i = 0; i < fechas.length; i++) {
				try{
					fechaSinHora = LocalDate.parse(fechas[i]);
				}catch(Exception e) {
					logger.log(Level.SEVERE, "Error al parsear fecha en el servlet ActualizarRegistro", e);
					request.setAttribute("errorGlobal", "No se ha podido parsear la fecha de obtención. ");
					return;
				}
			    LocalDateTime fecha = fechaSinHora.atStartOfDay();
			    String tipo = tipos[i];
			    String archivo = archivos.get(i);
				try{
					te = new TipoEvidencia(Integer.parseInt(tipo));
				}catch(Exception e) {
					logger.log(Level.SEVERE, "Error al parsear tipo de evidencia en el servlet ActualizarRegistro", e);
					request.setAttribute("errorGlobal", "No se ha podido parsear el tipo de evidencia. ");
					return;
				}
				try{
					te = controladorTipoEvidencia.getOne(te);
				}catch(Exception e) {
					logger.log(Level.SEVERE, "Error al obtener tipo de evidencia en el servlet ActualizarRegistro", e);
					request.setAttribute("errorGlobal", "No se ha podido obtener el tipo de evidencia. ");
					return;
				}
			    String estadoRegistro = "pendiente";
				if(usuario.getEstado().equals("investigador")) {
					estadoRegistro = "aprobado";
				}
			    Evidencia evidencia = new Evidencia(0,fecha,estadoRegistro,archivo,te);
				try{
					controladorEvidencia.save(evidencia);
				}catch(Exception e) {
					logger.log(Level.SEVERE, "Error al guardar evidencia en el servlet ActualizarRegistro", e);
					request.setAttribute("errorGlobal", "No se ha podido guardar la evidencia. ");
					return;
				}
			    evidencias.add(evidencia);
			}
			bestia.setEvidencias(evidencias);
			try{
				controladorBestia.saveEvidencias(bestia);
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al guardar evidencias de la bestia en el servlet ActualizarRegistro", e);
				request.setAttribute("errorGlobal", "No se ha podido guardar las evidencias dentro de la bestia. ");
				return;
			}
			
		}
		if(historia != null || introduccion != null || resumen != null || descripcion != null) {
			String estadoRegistro = null;
			LocalDateTime fechaAprobacion = null;
			Investigador user = null;
			if(usuario.getEstado().equals("investigador")) {
				estadoRegistro = "aprobado";
				fechaAprobacion = LocalDateTime.now();
				user = (Investigador) usuario;
			}else {
				estadoRegistro = "pendiente";
			}
			Registro registro = new Registro(0, mainPic, introduccion, historia, descripcion, resumen, fechaAprobacion, null, user, estadoRegistro , bestia );

			try{
				controladorRegistro.save(registro);
			}catch(Exception e) {
				logger.log(Level.SEVERE, "Error al guardar registro en el servlet ActualizarRegistro", e);
				request.setAttribute("errorGlobal", "No se ha podido guardar el registro. ");
				return;
			}
		}
		response.sendRedirect(HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) + "?id=" + bestiaId);
	}
	

}
