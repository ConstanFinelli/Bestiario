package helpers;

public final class HttpRoutes {

	// Root
	public static String HOME_JSP(String base) {
		return base + "/home.jsp";
	}

	// Auth
	public static String LOGIN_JSP(String base) {
		return base + "/auth/login.jsp";
	}
	
	public static String LOGIN(String base) {
		return base + "/auth/login";
	}

	public static String LOGOUT_JSP(String base) {
		return base + "/auth/logout.jsp";
	}

	public static String REGISTER_JSP(String base) {
		return base + "/auth/register.jsp";
	}
	
	public static String REGISTER(String base) {
		return base + "/auth/register";
	}
	
	// Imagen
	
	public static String IMAGENES(String base) {
		return base + "/imagenes/obtener";
	}
	

	// Bestias
	public static String BESTIA_FORMS_JSP(String base) {
		return base + "/bestias/bestiaForms.jsp";
	}

	public static String BESTIA_LIST_JSP(String base) {
		return base + "/bestias/bestias.jsp";
	}
	
	public static String CREAR_PROPUESTA_BESTIA_JSP(String base) {
		return base + "/bestias/crearPropuestaBestia.jsp";
	}
	
	public static String LISTAR_BESTIAS(String base) {
		return base + "/bestias/listar";
	}
	
	public static String CAMBIAR_HABITAT(String base) {
		return base + "/bestias/cambiarHabitat";
	}
	
	public static String CAMBIAR_CATEGORIA(String base) {
		return base + "/bestias/cambiarCategoria";
	}
	
	public static String ACTUALIZAR_BESTIA(String base) {
		return base + "/bestias/actualizar";
	}
	
	public static String ELIMINAR_BESTIA(String base) {
		return base + "/bestias/eliminar";
	}
	
	public static String CREAR_BESTIA(String base) {
		return base + "/bestias/crear";
	}

	// Registros
	public static String REGISTRO_JSP(String base) {
		return base + "/registros/registro.jsp";
	}

	public static String REGISTROS_PENDIENTES_JSP(String base) {
		return base + "/registros/registrosPendientes.jsp";
	}

	public static String ACTUALIZACION_REGISTRO_JSP(String base) {
		return base + "/registros/nuevoRegistro.jsp";
	}
	
	public static String ACEPTAR_REGISTRO(String base) {
		return base + "/registros/aceptarRegistro";
	}
	
	public static String ACTUALIZAR_REGISTRO(String base) {
		return base + "/registros/actualizarRegistro";
	}
	
	public static String OBTENER_REGISTRO_BESTIA(String base) {
		return base + "/registros/obtenerRegistroConBestia";
	}
	
	public static String OBTENER_REGISTROS_PENDIENTES_BESTIA(String base) {
		return base + "/registros/obtenerRegistrosPendientesBestia";
	}
	
	public static String OBTENER_REGISTRO_BESTIA_CON_FECHA(String base) {
		return base + "/registros/obtenerRegistroConBestiaYFecha";
	}

	// Habitats
	public static String CARAC_HABITAT_FORM_JSP(String base) {
		return base + "/habitats/carHabitatForms.jsp";
	}

	public static String HABITAT_FORM_JSP(String base) {
		return base + "/habitats/habitatForms.jsp";
	}

	public static String HABITATS_JSP(String base) {
		return base + "/habitats/habitats.jsp";
	}
	
	public static String LISTAR_HABITATS(String base) {
		return base + "/habitats/listar";
	}
	
	public static String CREAR_HABITAT(String base) {
		return base + "/habitats/crear";
	}
	
	public static String ACTUALIZAR_HABITAT(String base) {
		return base + "/habitats/actualizar";
	}
	
	public static String ELIMINAR_HABITAT(String base) {
		return base + "/habitats/eliminar";
	}
	
	public static String OBTENER_HABITAT(String base) {
		return base + "/habitats/obtener";
	}
	
	public static String LISTAR_CARACTERISTICAHABITAT(String base) {
		return base + "/habitats/listarCaracteristicaHabitat";
	}
	
	public static String CREAR_CARACTERISTICAHABITAT(String base) {
		return base + "/habitats/crearCaracteristicaHabitat";
	}
	
	public static String ACTUALIZAR_CARACTERISTICAHABITAT(String base) {
		return base + "/habitats/actualizarCaracteristicaHabitat";
	}

	public static String ELIMINAR_CARACTERISTICAHABITAT(String base) {
		return base + "/habitats/eliminarCaracteristicaHabitat";
	}

	// Noticias
	public static String NOTICIA_FORM_JSP(String base) {
		return base + "/noticias/noticiaForms.jsp";
	}
	
	public static String LISTAR_NOTICIAS(String base) {
		return base + "/noticias/listar";
	}
	
	public static String CREAR_NOTICIA(String base) {
		return base + "/noticias/crear";
	}
	
	public static String NOTICIAS_JSP(String base) {
		return base + "/noticias/noticias.jsp";
	}
	
	public static String REDACTARNOTICIA_JSP(String base) {
		return base + "/noticias/redactarNoticia.jsp";
	}

	// Categorias
	public static String CATEGORIA_FORM_JSP(String base) {
		return base + "/categorias/categoriaForms.jsp";
	}
	
	public static String OBTENER_CATEGORIA(String base) {
		return base + "/categorias/obtener";
	}
	
	public static String LISTAR_CATEGORIAS(String base) {
		return base + "/categorias/listar";
	}
	
	public static String CREAR_CATEGORIA(String base) {
		return base + "/categorias/crear";
	}
	
	public static String ELIMINAR_CATEGORIA(String base) {
		return base + "/categorias/eliminar";
	}
	
	public static String ACTUALIZAR_CATEGORIA(String base) {
		return base + "/categorias/actualizar";
	}

	// Evidencias
	public static String EVIDENCIA_FORM_JSP(String base) {
		return base + "/evidencias/evidenciaForms.jsp";
	}

	public static String TIPO_EVIDENCIA_FORM_JSP(String base) {
		return base + "/evidencias/tipoEvidenciaForms.jsp";
	}
	
	public static String OBTENER_EVIDENCIA(String base) {
		return base + "/evidencias/obtener";
	}
	
	public static String LISTAR_EVIDENCIAS(String base) {
		return base + "/evidencias/listar";
	}
	
	public static String LISTAR_EVIDENCIAS_TIPO(String base) {
		return base + "/evidencias/listarPorTipo";
	}
	
	public static String CREAR_EVIDENCIA(String base) {
		return base + "/evidencias/crear";
	}
	
	public static String ACTUALIZAR_EVIDENCIA(String base) {
		return base + "/evidencias/actualizar";
	}
	
	public static String ELIMINAR_EVIDENCIA(String base) {
		return base + "/evidencias/eliminar";
	}
	
	public static String OBTENER_TIPO_EVIDENCIA(String base) {
		return base + "/evidencias/obtenerTipoEvidencia";
	}
	
	public static String LISTAR_TIPOS_EVIDENCIA(String base) {
		return base + "/evidencias/listarTiposEvidencia";
	}
	
	public static String CREAR_TIPO_EVIDENCIA(String base) {
		return base + "/evidencias/crearTipoEvidencia";
	}
	
	public static String ACTUALIZAR_TIPO_EVIDENCIA(String base) {
		return base + "/evidencias/actualizarTipoEvidencia";
	}
	
	public static String ELIMINAR_TIPO_EVIDENCIA(String base) {
		return base + "/evidencias/eliminarTipoEvidencia";
	}
	

	// Investigadores
	public static String SOLICITUDES_INVESTIGADOR_JSP(String base) {
		return base + "/investigadores/solicitudesInvestigador.jsp";
	}
	
	public static String PRESENTAR_CANDIDATURA_JSP(String base) {
		return base + "/investigadores/presentarCandidatura.jsp";
	}
	
	public static String LISTAR_SOLICITANTES(String base) {
		return base + "/investigadores/listarSolicitantes";
	}
	
	public static String APROBAR_SOLICITUD(String base) {
		return base + "/investigadores/aprobarSolicitud";
	}
	
	public static String RECHAZAR_SOLICITUD(String base) {
		return base + "/investigadores/rechazarSolicitud";
	}
	
	public static String CREAR_SOLICITUD(String base) {
		return base + "/investigadores/crearSolicitud";
	}
	
	// Comentarios
	public static String AGREGAR_COMENTARIO(String base) {
		return base + "/comentarios/agregar";
	}
	
	// Estilos
	
	public static String MAIN_CSS(String base) {
		return base + "/css/main.css";
	}
	
	public static String HOME_CSS(String base) {
		return base + "/css/home.css";
	}
	
	public static String NAVBAR_CSS(String base) {
		return base + "/css/navbar.css";
	}
	
	public static String BESTIAS_CSS(String base) {
		return base + "/css/bestias.css";
	}

	public static String HABITATS_CSS(String base) {
		return base + "/css/habitats.css";
	}
	
	public static String LOGIN_CSS(String base) {
		return base + "/css/login.css";
	}
	
	public static String NOTICIAS_CSS(String base) {
		return base + "/css/noticias.css";
	}
	
	public static String NUEVABESTIA_CSS(String base) {
		return base + "/css/nuevaBestia.css";
	}
	
	public static String REGISTRO_CSS(String base) {
		return base + "/css/registro.css";
	}
	
	public static String REGISTROS_CSS(String base) {
		return base + "/css/registros.css";
	}
	
	public static String REDACTARNOTICIA_CSS(String base) {
		return base + "/css/redactarNoticia.css";
	}
	
	
	
}