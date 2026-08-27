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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
import helpers.EnvHelper;

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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Usuario usuario = (Usuario) session.getAttribute("user");
		String bestiaId = request.getParameter("id");

		Bestia bestia = null;
		try {
			bestia = new Bestia(Integer.parseInt(bestiaId));
			bestia = controladorBestia.getOne(bestia);
			if(bestia == null) {
				request.setAttribute("errorGlobal", "La bestia especificada no fue encontrada.");
				request.getRequestDispatcher(HttpRoutes.HOME_JSP("")).forward(request, response);
				return;
			}
		} catch(NumberFormatException e) {
			logger.log(Level.WARNING, "Error al parsear idBestia en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "El id de la bestia es inválido.");
			request.getRequestDispatcher(HttpRoutes.HOME_JSP("")).forward(request, response);
			return;
		} catch(Exception e) {
			logger.log(Level.SEVERE, "Error al conseguir bestia en el servlet ActualizarRegistro", e);
			request.setAttribute("errorGlobal", "No se ha conseguido la bestia.");
			request.getRequestDispatcher(HttpRoutes.HOME_JSP("")).forward(request, response);
			return;
		}

		String introduccion = request.getParameter("introduccion");
		String resumen = request.getParameter("resumen");
		String historia = request.getParameter("historia");
		String descripcion = request.getParameter("descripcion");

		Registro registroActual = null;
		try {
			registroActual = controladorRegistro.getRegistroToShow(bestia, LocalDateTime.now());
		} catch(Exception e) {
			logger.log(Level.WARNING, "Error al conseguir registro actual en el servlet ActualizarRegistro", e);
		}

		List<String> uploadedCloudinaryIds = new ArrayList<>();
		Part filePart = null;
		try {
			filePart = request.getPart("mainPic");
		} catch(Exception e) {
			logger.log(Level.WARNING, "Error al leer part mainPic", e);
		}

		boolean hasNewMainPic = (filePart != null && filePart.getSize() > 0);
		boolean hasTextChanged;
		if (registroActual == null) {
			hasTextChanged = (introduccion != null && !introduccion.trim().isEmpty())
					|| (historia != null && !historia.trim().isEmpty())
					|| (descripcion != null && !descripcion.trim().isEmpty())
					|| (resumen != null && !resumen.trim().isEmpty());
		} else {
			hasTextChanged = !Objects.equals(introduccion, registroActual.getIntroduccion())
					|| !Objects.equals(historia, registroActual.getHistoria())
					|| !Objects.equals(descripcion, registroActual.getDescripcion())
					|| !Objects.equals(resumen, registroActual.getResumen());
		}

		try {
			// 1. Guardar Registro si hubo nueva imagen o cambio en los textos
			if (hasNewMainPic || hasTextChanged) {
				String mainPic;
				if (hasNewMainPic) {
					mainPic = CloudinaryHelper.upload(filePart);
					uploadedCloudinaryIds.add(mainPic);
				} else {
					mainPic = (registroActual != null) ? registroActual.getMainPic() : EnvHelper.get("DEFAULT_PICTURE_ID");
				}

				if (registroActual != null) {
					if (introduccion == null || introduccion.trim().isEmpty()) introduccion = registroActual.getIntroduccion();
					if (historia == null || historia.trim().isEmpty()) historia = registroActual.getHistoria();
					if (descripcion == null || descripcion.trim().isEmpty()) descripcion = registroActual.getDescripcion();
					if (resumen == null || resumen.trim().isEmpty()) resumen = registroActual.getResumen();
				}

				String estadoRegistro = "pendiente";
				LocalDateTime fechaAprobacion = null;
				Investigador user = null;
				if (usuario != null && "investigador".equals(usuario.getEstado())) {
					estadoRegistro = "aprobado";
					fechaAprobacion = LocalDateTime.now();
					user = (Investigador) usuario;
				}

				Registro nuevoRegistro = new Registro(0, mainPic, introduccion, historia, descripcion, resumen, fechaAprobacion, null, user, estadoRegistro, bestia);
				controladorRegistro.save(nuevoRegistro);
			}

			// 2. Procesar evidencias
			String[] fechas = request.getParameterValues("fechaObtencion");
			String[] tipos = request.getParameterValues("tipo");
			Collection<Part> parts = request.getParts();
			LinkedList<String> archivos = new LinkedList<>();

			for (Part p : parts) {
				if ("archivo".equals(p.getName()) && p.getSize() > 0) {
					String id = CloudinaryHelper.upload(p);
					archivos.add(id);
					uploadedCloudinaryIds.add(id);
				}
			}

			if (fechas != null && !archivos.isEmpty()) {
				LinkedList<Evidencia> evidencias = new LinkedList<>();
				for (int i = 0; i < fechas.length && i < archivos.size() && i < tipos.length; i++) {
					LocalDate fechaSinHora = LocalDate.parse(fechas[i]);
					LocalDateTime fecha = fechaSinHora.atStartOfDay();
					TipoEvidencia te = controladorTipoEvidencia.getOne(new TipoEvidencia(Integer.parseInt(tipos[i])));
					String estadoEvidencia = (usuario != null && "investigador".equals(usuario.getEstado())) ? "aprobado" : "pendiente";
					Evidencia evidencia = new Evidencia(0, fecha, estadoEvidencia, archivos.get(i), te);
					controladorEvidencia.save(evidencia);
					evidencias.add(evidencia);
				}

				if (!evidencias.isEmpty()) {
					bestia.setEvidencias(evidencias);
					controladorBestia.saveEvidencias(bestia);
				}
			}

		} catch(Exception e) {
			logger.log(Level.SEVERE, "Error durante la actualización del registro / evidencias", e);
			// Rollback de imágenes subidas a Cloudinary en esta petición para evitar huérfanas
			for (String id : uploadedCloudinaryIds) {
				try {
					CloudinaryHelper.deleteImage(id);
				} catch(Exception ignored) {}
			}
			request.setAttribute("errorGlobal", "Ocurrió un error al guardar los cambios: " + e.getMessage());
			request.getRequestDispatcher(HttpRoutes.ACTUALIZACION_REGISTRO_JSP("")).forward(request, response);
			return;
		}

		response.sendRedirect(HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) + "?id=" + bestiaId);
	}
	

}
