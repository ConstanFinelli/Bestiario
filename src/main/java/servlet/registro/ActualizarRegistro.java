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
import logic.LogicContenidoRegistro;
import logic.LogicEvidencia;
import logic.LogicRegistro;
import logic.LogicTipoEvidencia;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;

import entities.Bestia;
import entities.ContenidoRegistro;
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
@MultipartConfig
@WebServlet("/registros/actualizarRegistro")
public class ActualizarRegistro extends HttpServlet {
	private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
	private LogicBestia controladorBestia = new LogicBestia();
	private LogicRegistro controladorRegistro = new LogicRegistro();
	private LogicEvidencia controladorEvidencia = new LogicEvidencia();
	private LogicContenidoRegistro controladorCr = new LogicContenidoRegistro();
	
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
		Bestia bestia = new Bestia(Integer.parseInt(idBestia));
		bestia = controladorBestia.getOne(bestia);
		LinkedList<TipoEvidencia> tes = controladorTipoEvidencia.findAll();
		request.setAttribute("bestia", bestia);
		request.setAttribute("tiposEvidencia", tes);
		request.setAttribute("foundBestia", bestia);
		
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
		Bestia bestia = new Bestia(Integer.parseInt(bestiaId));
		
		Part filePart = request.getPart("mainPic");
		if(filePart != null && filePart.getSize() > 0 ) {
			mainPic = CloudinaryHelper.upload(filePart);
		}else if(controladorRegistro.getRegistroToShow(new Bestia(Integer.parseInt(bestiaId), null, null, null), LocalDateTime.now()) != null){
			mainPic = controladorRegistro.getRegistroToShow(new Bestia(Integer.parseInt(bestiaId), null, null, null), LocalDateTime.now()).getMainPic();
		}
		
		
		ContenidoRegistro contenido = new ContenidoRegistro(0, introduccion, historia,descripcion, resumen);
		
		Registro registroActual = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		if(registroActual != null) {
			ContenidoRegistro crActual = registroActual.getContenido();	
			if(contenido.getDescripcion().equals(crActual.getDescripcion()) && contenido.getHistoria().equals(crActual.getHistoria()) 
					&& contenido.getIntroduccion().equals(crActual.getIntroduccion()) && contenido.getResumen().equals(crActual.getResumen()))
			{ // verificar si no hubo modificaciones
				contenido = null;
			}
		}
		bestia = controladorBestia.getOne(new Bestia(Integer.parseInt(bestiaId),null,null, null));
		
		String[] fechas = request.getParameterValues("fechaObtencion");
		String[] tipos = request.getParameterValues("tipo");
		String[] links = request.getParameterValues("link");
		
		
		if(fechas != null) {
			LinkedList<Evidencia> evidencias = new LinkedList<>();
			for (int i = 0; i < fechas.length; i++) {
				LocalDate fechaSinHora = LocalDate.parse(fechas[i]);
			    LocalDateTime fecha = fechaSinHora.atStartOfDay();
			    String tipo = tipos[i];
			    String link = links[i];
			    TipoEvidencia te = new TipoEvidencia(Integer.parseInt(tipo), null);
			    te = controladorTipoEvidencia.getOne(te);
			    String estadoRegistro = "pendiente";
				if(usuario.getEstado().equals("investigador")) {
					estadoRegistro = "aprobado";
				}
			    Evidencia evidencia = new Evidencia(0,fecha,estadoRegistro,link,te);
			    controladorEvidencia.save(evidencia);
			    evidencias.add(evidencia);
			}
			bestia.setEvidencias(evidencias);
			controladorBestia.saveEvidencias(bestia);
			
		}
		if(contenido != null) {
			contenido = controladorCr.save(contenido);
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
			Registro registro = new Registro(0, mainPic, contenido, fechaAprobacion, null, user, estadoRegistro , bestia );
			
			controladorRegistro.save(registro);
		}
		response.sendRedirect(HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) + "?id=" + bestiaId);
	}
	

}
