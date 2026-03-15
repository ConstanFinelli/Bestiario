	package servlet;
	
	import jakarta.servlet.RequestDispatcher;
	import jakarta.servlet.ServletException;
	import jakarta.servlet.annotation.MultipartConfig;
	import jakarta.servlet.annotation.WebServlet;
	import jakarta.servlet.http.HttpServlet;
	import jakarta.servlet.http.HttpServletRequest;
	import jakarta.servlet.http.HttpServletResponse;
	import jakarta.servlet.http.HttpSession;
	import jakarta.servlet.http.Part;

	import java.io.IOException;
	import java.time.LocalDate;
	import java.time.LocalDateTime;

	import java.util.LinkedList;
	
	import entities.Bestia;
	import entities.Categoria;
	import entities.Comentario;
	import entities.ContenidoRegistro;
	import entities.Evidencia;
	import entities.Habitat;
	import entities.Investigador;
	import entities.Registro;
	import entities.TipoEvidencia;
	import entities.Usuario;
	import logic.LogicBestia;
	import logic.LogicCategoria;
	import logic.LogicComentario;
	import logic.LogicContenidoRegistro;
	import logic.LogicEvidencia;
import logic.LogicHabitat;
import logic.LogicRegistro;
	import logic.LogicTipoEvidencia;
	import logic.LogicUsuario;
import helpers.Constantes;
import helpers.HttpMethodParser;
import helpers.ImagesHelper;
	/**
	 * Servlet implementation class SbBestia
	 */
	@MultipartConfig
	@WebServlet("/SvBestia")
	public class SvBestia extends HttpServlet {
		
		
		//CONSTANTES
		private final String BESTIA_NOT_FOUND = "No existe una bestia con ese id";
		private final String CREATE_BESTIA_ERROR = "Error al crear la nueva bestia. Revisar datos enviados";
		private final String ID_FORMAT_ERROR = "Error en el formato del id ingresado";

		//private final String ADD_BESTIA_JSP = "crearPropuestaBestia.jsp"; 
		private LogicBestia controlador = new LogicBestia();
		private LogicRegistro controladorRegistro = new LogicRegistro();
		private LogicComentario controladorComentario = new LogicComentario();
		private LogicUsuario controladorUsuario = new LogicUsuario();
		private LogicContenidoRegistro controladorCr = new LogicContenidoRegistro();
		private LogicEvidencia controladorEvidencia = new LogicEvidencia();
		private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
		private LogicHabitat controladorHabitat = new LogicHabitat();
		private LogicCategoria controladorCategoria = new LogicCategoria();
		
		private static final long serialVersionUID = 1L;
		
		// HELPER
		private HttpMethodParser parser = new HttpMethodParser();   
	
		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
		 */
		//##GET ONE##
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	String id = request.getParameter("id");
			String fechaParam = request.getParameter("fecha");
			String nroRegistro = request.getParameter("nroRegistro");
			
			RequestDispatcher rd = null;
			
			HttpSession session = request.getSession();
			
			LinkedList<Bestia> bestias = controlador.findAll();
			session.setAttribute("bestias", bestias);
			
			Registro registro = null;
			LinkedList<Registro> registrosPendientes = new LinkedList<>();
			LocalDate fechaSinTime = null;
			LocalDateTime fecha = null;
			
			if (fechaParam != null && !fechaParam.isEmpty()) {
			    fechaSinTime = LocalDate.parse(fechaParam);
			    fecha = fechaSinTime.atStartOfDay();
			} else{
				fecha = LocalDateTime.now();
			};
			
			String action = request.getParameter("action");
			
			if("form".equals(action)) {
				rd = request.getRequestDispatcher(Constantes.BESTIA_FORMS_JSP);
				LinkedList<Habitat> habitats = controladorHabitat.findAll();
				LinkedList <Categoria> categorias = controladorCategoria.findAll();
				session.setAttribute("habitats", habitats);
				session.setAttribute("categorias", categorias);
			}else if("list".equals(action)){
				rd = request.getRequestDispatcher(Constantes.BESTIA_LIST_JSP);
			}else if("registro".equals(action)) {
				rd = request.getRequestDispatcher(Constantes.REGISTRO_JSP);
			}else if ("registrosPendientes".equals(action)) {
				rd = request.getRequestDispatcher(Constantes.REGISTROS_PENDIENTES_JSP);
			}
			else if("actualizacion".equals(action)) {
				rd = request.getRequestDispatcher(Constantes.ACTUALIZACION_REGISTRO_JSP);
				LinkedList<TipoEvidencia> tes = controladorTipoEvidencia.findAll();
				request.setAttribute("tes", tes);
			}
			
			String getOneMsg = "";
			if(id != null) {
				Bestia bestia = new Bestia(Integer.parseInt(id));
				bestia = controlador.getOne(bestia);
				if(bestia != null) {
					getOneMsg = getOneMsg + bestia + "<br><br>";
					if("registrosPendientes".equals(action)) {
						registrosPendientes = controladorRegistro.findRegistrosPendientes(bestia);
						
					} else {
						if(nroRegistro != null) {
							registro = new Registro(Integer.parseInt(nroRegistro), null, null, null, null, null, null, bestia);
							
							registro = controladorRegistro.getOne(registro);
						} else {
							registro = controladorRegistro.getRegistroToShow(bestia, fecha);	
						}
					}		
				}else {
					getOneMsg = BESTIA_NOT_FOUND;
					
				}	
				request.setAttribute("getOneMsg", getOneMsg);
				request.setAttribute("bestia", bestia);
				session.setAttribute("registro", registro);
				request.setAttribute("registrosPendientes", registrosPendientes);
			} 
			rd.forward(request, response);
		}
	
		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
	
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			RequestDispatcher rd = request.getRequestDispatcher(Constantes.BESTIA_FORMS_JSP); 
			HttpSession session = request.getSession();
			
			String flag = request.getParameter("flag");
			String nombre = request.getParameter("nombre");
			String peligrosidad= request.getParameter("peligrosidad");
			String action = request.getParameter("action");
			
			Usuario usuario = (Usuario) session.getAttribute("user");
			
			boolean redirigido = false;
			
			try {
				redirigido = parser.Redirect(flag, new HttpMethodParser.MethodCallback() {
			        @Override
			        public void put() throws ServletException, IOException {
		        		doPut(request, response);	
			        }
	
			        @Override
			        public void delete() throws IOException, ServletException {
		        		doDelete(request,response);
			        }
			    });
			}catch(IOException e) {
        		e.printStackTrace();
        	}catch(ServletException e) {
        		e.printStackTrace();
        	}

		    // 3. Si el parser devuelve true (caso PATCH en tu switch) o manejas lógica extra
		    if (redirigido) {
		        return;
		    }
			
			// 4. Validar accion y ejecutar flujo correspondiente
		    if(action.equals("actualizacion")) {
				rd = request.getRequestDispatcher(Constantes.ACTUALIZACION_REGISTRO_JSP);
				String bestiaId = doProposeRegistro(request, response, session);
				response.sendRedirect("SvBestia?action=registro&id="+bestiaId);
				return;
			}else if(action.equals("add")) {
				   doAddBestiaProposal(request, response);
				   return;
			}
		    
		    if (flag == "") {
				String idBestia = doAddCommentary(request,response);
				String redirectUrl = request.getContextPath() + "/SvBestia?action=registro&id=" + idBestia;
				response.sendRedirect(redirectUrl);
		    }
		    
		    // en caso de no tener accion valida ejecutar flujo del post
			try {
				if (nombre != null && peligrosidad != null) {
					String estado = "pendiente";
					if (usuario.getEstado().equals("investigador")) {
						estado = "aprobado";
					} 
					Bestia bestia = new Bestia(nombre, peligrosidad, estado);
					bestia = controlador.save(bestia);
				} 
			} catch (NumberFormatException e) {
				e.getMessage();
			} 
		
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
			String estado = request.getParameter("estado");
			String putAction = request.getParameter("putAction");
			
			if(putAction == null) {
				try {
					Bestia bestia = new Bestia(Integer.parseInt(id), nombre, peligrosidad, estado);
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
			}else {
				Bestia bestia = controlador.getOne(new Bestia(Integer.parseInt(id), null, null, null));
				String idHabitat = request.getParameter("idHabitat");
				String idCategoria = request.getParameter("idCategoria");
				if("addRelations".equals(putAction)) {
					if(idHabitat != null) {
						doSaveHabitats(request, response, bestia, idHabitat);
					}else if(idCategoria != null) {
						doSaveCategorias(request, response, bestia, idCategoria);
					}
				}else if("removeRelation".equals(putAction)) {
					if(idHabitat != null) {
						doRemoveHabitats(request, response, bestia, idHabitat);
					}else if(idCategoria != null) {
						doRemoveCategorias(request, response, bestia, idCategoria);
					}
				}
			}
			request.getRequestDispatcher(Constantes.BESTIA_FORMS_JSP).forward(request, response);
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
			response.sendRedirect(HttpRoutes.LISTAR_BESTIAS(request.getContextPath()));
		}
		
		protected void doAddBestiaProposal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			String nombre = request.getParameter("nombre");
			String peligrosidad = request.getParameter("peligrosidad");
			String estado = request.getParameter("estado");
			Bestia newBestia = new Bestia(nombre, peligrosidad, estado);
			newBestia = controlador.save(newBestia);
			response.sendRedirect("SvBestia?action=registro&id=" + newBestia.getIdBestia());
		}
		
		protected String doAddCommentary(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			String contenido = request.getParameter("contenido");
			String idUsuario = request.getParameter("idUsuario");
			String idBestia = request.getParameter("idBestia");
			if(contenido != null && idUsuario != null && idBestia != null) {
				LocalDateTime fechaComentario = LocalDateTime.now();
				Usuario publicador = controladorUsuario.getOne(new Usuario(Integer.parseInt(idUsuario)));
				Bestia bestia = controlador.getOne(new Bestia(Integer.parseInt(idBestia),null,null, null));
				Comentario comentario = new Comentario(publicador, bestia, fechaComentario, contenido);
				controladorComentario.save(comentario);
			}
			return idBestia;
		}
		
		protected String doProposeRegistro(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException{
			String introduccion = request.getParameter("introduccion");
			String resumen = request.getParameter("resumen");
			String historia = request.getParameter("historia");
			String descripcion = request.getParameter("descripcion");
			String mainPic = null;
			
			Usuario usuario = (Usuario) session.getAttribute("user");	
			String bestiaId = request.getParameter("id");
			
			Part filePart = request.getPart("mainPic");
			if(filePart != null && filePart.getSize() > 0 ) {
				mainPic = ImagesHelper.saveImage(filePart, controladorRegistro.obtenerNombreImagen(bestiaId));
			}else if(controladorRegistro.getRegistroToShow(new Bestia(Integer.parseInt(bestiaId), null, null, null), LocalDateTime.now()) != null){
				mainPic = controladorRegistro.getRegistroToShow(new Bestia(Integer.parseInt(bestiaId), null, null, null), LocalDateTime.now()).getMainPic();
			}
			
			
			
			ContenidoRegistro contenido = new ContenidoRegistro(0, introduccion, historia,descripcion, resumen);
			
			Registro registroActual = (Registro) session.getAttribute("registro");
			if(registroActual != null) {
				ContenidoRegistro crActual = registroActual.getContenido();	
				if(contenido.getDescripcion() == crActual.getDescripcion() && contenido.getHistoria() == crActual.getHistoria() 
						&& contenido.getIntroduccion() == crActual.getIntroduccion() && contenido.getResumen() == crActual.getResumen()) { // verificar si no hubo modificaciones
					descripcion = null;
					introduccion = null;
					resumen = null;
					historia = null;
				}
			}
			Bestia bestia = controlador.getOne(new Bestia(Integer.parseInt(bestiaId),null,null, null));
			
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
				controlador.saveEvidencias(bestia);
				
			}
			if(introduccion != null && resumen != null && historia != null && descripcion != null) {
				contenido = controladorCr.save(contenido);
				String estadoRegistro = "pendiente";
				LocalDateTime fechaAprobacion = null;
				Investigador user = null;
				if(usuario.getEstado().equals("investigador")) {
					estadoRegistro = "aprobado";
					fechaAprobacion = LocalDateTime.now();
					user = (Investigador) usuario;
				}
				Registro registro = new Registro(0, mainPic, contenido, fechaAprobacion, null, user, estadoRegistro , bestia );
				
				controladorRegistro.save(registro);
			}
			return bestiaId;
		}
		
		protected void doSaveHabitats(HttpServletRequest request, HttpServletResponse response, Bestia bestia, String idHabitat) throws ServletException, IOException{
			Habitat ht = controladorHabitat.getOne(new Habitat(Integer.parseInt(idHabitat),null,null,null));
			LinkedList<Habitat> habitatsBestia = bestia.getHabitats();
			boolean isIn = false;
			for(Habitat habitat:habitatsBestia) {
				if(habitat.getId() == ht.getId()) {
					isIn = true;
			}
			}
			if(!isIn) {
				habitatsBestia.add(ht);
				bestia.setHabitats(habitatsBestia);
				controlador.saveHabitats(bestia);
			}else {
				request.setAttribute("errorMsg", "Habitat ya se encuentra asignada a la bestia.");
			}
		}
		
		protected void doSaveCategorias(HttpServletRequest request, HttpServletResponse response, Bestia bestia, String idCategoria) throws ServletException, IOException{
			Categoria cat = controladorCategoria.getOne(new Categoria(Integer.parseInt(idCategoria),null,null));
			LinkedList<Categoria> categoriasBestia = bestia.getCategorias();
			boolean isIn = false;
			for(Categoria categoria:categoriasBestia) {
				if(categoria.getIdCategoria() == cat.getIdCategoria()) {
					isIn = true;
			}
			}
			if(!isIn) {
				categoriasBestia.add(cat);
				bestia.setCategorias(categoriasBestia);
				controlador.saveCategorias(bestia);
			}else {
				request.setAttribute("errorMsg", "Categoria ya se encuentra asignada a la bestia.");
			}
		}
		
		protected void doRemoveHabitats(HttpServletRequest request, HttpServletResponse response, Bestia bestia, String idHabitat) throws ServletException, IOException{
			Habitat ht = controladorHabitat.getOne(new Habitat(Integer.parseInt(idHabitat),null,null,null));
			LinkedList<Habitat> habitatsBestia = bestia.getHabitats();
			boolean isIn = false;
			for(Habitat habitat:habitatsBestia) {
				if(habitat.getId() == ht.getId()) {
					isIn = true;
			}
			}
			if(isIn) {
				controlador.removeRelation(bestia,ht);
			}else {
				request.setAttribute("errorMsg", "Habitat no se encuentra asignada a la bestia.");
			}
		}
		
		protected void doRemoveCategorias(HttpServletRequest request, HttpServletResponse response, Bestia bestia, String idCategoria) throws ServletException, IOException{
			Categoria cat = controladorCategoria.getOne(new Categoria(Integer.parseInt(idCategoria),null,null));
			LinkedList<Categoria> categoriasBestia = bestia.getCategorias();
			boolean isIn = false;
			for(Categoria categoria:categoriasBestia) {
				if(categoria.getIdCategoria() == cat.getIdCategoria()) {
					isIn = true;
			}
			}
			if(isIn) {
				controlador.removeRelation(bestia,cat);
			}else {
				request.setAttribute("errorMsg", "Categoria no se encuentra asignada a la bestia.");
			}
		}
}