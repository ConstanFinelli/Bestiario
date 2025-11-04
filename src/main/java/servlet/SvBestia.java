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
	
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.IOException;
	import java.io.OutputStream;
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
	@MultipartConfig
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
		private final String REGISTROS_PENDIENTES_JSP = "registrosPendientes.jsp";
		private final String ACTUALIZACION_REGISTRO_JSP = "nuevoRegistro.jsp";
		private LogicBestia controlador = new LogicBestia();
		private LogicRegistro controladorRegistro = new LogicRegistro();
		private LogicComentario controladorComentario = new LogicComentario();
		private LogicUsuario controladorUsuario = new LogicUsuario();
		private LogicContenidoRegistro controladorCr = new LogicContenidoRegistro();
		private LogicEvidencia controladorEvidencia = new LogicEvidencia();
		private LogicTipoEvidencia controladorTipoEvidencia = new LogicTipoEvidencia();
		
		private String getExternalFolder() {
			return ("C:/Users/costa/OneDrive/Escritorio/Facu/java/Bestiario/docs/imagenes"); 
			//Ruta absoluta temporalmente
		}
		
		private static final long serialVersionUID = 1L;
	       
	
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
			
			Registro registro = null;
			LinkedList<Registro> registrosPendientes = new LinkedList<>();
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
			}else if ("registrosPendientes".equals(action)) {
				rd = request.getRequestDispatcher(REGISTROS_PENDIENTES_JSP);
			}
			else if("imagen".equals(action)) {
				doGetImage(request, response);
				return;
			}
			else if("actualizacion".equals(action)) {
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
					if("registrosPendientes".equals(action)) {
						registrosPendientes = controladorRegistro.findRegistrosPendientes(bestia);
						
					} else {
						if(nroRegistro != null) {
							registro = new Registro(Integer.parseInt(nroRegistro), null, null, null, null, null, null, bestia);
							
							registro = controladorRegistro.getOne(registro);
						} else {
							registro = controladorRegistro.getRegistroToShow(bestia, fecha);	
						}
						if (registro == null) {
		                    request.getSession().setAttribute("errorMsg", 
		                        REGISTRO_NOT_FOUND);
		                    response.sendRedirect("SvBestia?action=list");
		                    return; 
		                }
					}		
				}else {
					getOneMsg = BESTIA_NOT_FOUND;
					
				}	
				request.setAttribute("getOneMsg", getOneMsg);
				request.setAttribute("bestia", bestia);
				session.setAttribute("registro", registro);
				request.setAttribute("registrosPendientes", registrosPendientes);
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
				String mainPic = doPostImage(request, response);
				
				HttpSession session = request.getSession();
				
				ContenidoRegistro contenido = new ContenidoRegistro(0, introduccion, historia,descripcion, resumen);
				
				Registro registroActual = (Registro) session.getAttribute("registro");
				ContenidoRegistro crActual = registroActual.getContenido();
				
				if(contenido.getDescripcion() != crActual.getDescripcion() && contenido.getHistoria() != crActual.getHistoria() 
						&& contenido.getIntroduccion() != crActual.getIntroduccion() && contenido.getResumen() != crActual.getResumen()) { // verificar si no hubo modificaciones
					descripcion = null;
					introduccion = null;
					resumen = null;
					historia = null;
				}
				
				Bestia bestia = controlador.getOne(new Bestia(Integer.parseInt(bestiaId),null,null));
				
				String[] fechas = request.getParameterValues("fechaObtencion");
				String[] tipos = request.getParameterValues("tipo");
				String[] links = request.getParameterValues("link");
				
				Usuario usuario = (Usuario) session.getAttribute("user");
				
				if(fechas != null) {
					LinkedList<Evidencia> evidencias = new LinkedList<>();
					for (int i = 0; i < fechas.length; i++) {
					    LocalDate fecha = LocalDate.parse(fechas[i]);
					    String tipo = tipos[i];
					    String link = links[i];
					    TipoEvidencia te = new TipoEvidencia(Integer.parseInt(tipo), null);
					    te = controladorTipoEvidencia.getOne(te);
					    String estado = "pendiente";
						if(usuario.isEsInvestigador()) {
							estado = "aprobado";
						}
					    Evidencia evidencia = new Evidencia(0,fecha,estado,link,te);
					    controladorEvidencia.save(evidencia);
					    evidencias.add(evidencia);
					}
					bestia.setEvidencias(evidencias);
					controlador.saveEvidencias(bestia);
					
				}
				if(introduccion != null && resumen != null && historia != null && descripcion != null) {
					contenido = controladorCr.save(contenido);
					String estado = "pendiente";
					if(usuario.isEsInvestigador()) {
						estado = "aprobado";
					}
					Registro registro = new Registro(0, mainPic, contenido, null, null, null, estado , bestia );
					controladorRegistro.save(registro);
				}
				response.sendRedirect("SvBestia?action=registro&id="+bestiaId);
				return;
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
		
		protected void doGetImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("Buscando en: " + getExternalFolder());
			String fileName = request.getParameter("file");
	        if(fileName == null || fileName.isEmpty()) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file specified");
	            return;
	        }
	
	        File imageFile = new File(getExternalFolder(), fileName);
	        if(!imageFile.exists()) {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
	            return;
	        }
	
	     
	        String mimeType = getServletContext().getMimeType(imageFile.getName());
	        if(mimeType == null) {
	            mimeType = "application/octet-stream";
	        }
	
	        response.setContentType(mimeType);
	        response.setContentLengthLong(imageFile.length());
	
	        try (FileInputStream fis = new FileInputStream(imageFile);
	             OutputStream os = response.getOutputStream()) {
	
	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while((bytesRead = fis.read(buffer)) != -1) {
	                os.write(buffer, 0, bytesRead);
	            }
	        }
		}
		
		protected String doPostImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			Part filePart = request.getPart("mainPic");
	    	String fileName = filePart.getSubmittedFileName();
	    	
	    	File uploadDir = new File(getExternalFolder());
	    	if(!uploadDir.exists()) {
	    		uploadDir.mkdirs();
	    	}
	    	
	    	File file = new File(uploadDir, fileName);
	    	filePart.write(file.getAbsolutePath());
	    	return fileName; 
		}
	
	}