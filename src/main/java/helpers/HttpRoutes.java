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

	public static String LOGOUT_JSP(String base) {
		return base + "/auth/logout.jsp";
	}

	public static String REGISTER_JSP(String base) {
		return base + "/auth/register.jsp";
	}

	// Bestias
	public static String BESTIA_FORMS_JSP(String base) {
		return base + "/bestias/bestiaForms.jsp";
	}

	public static String BESTIA_LIST_JSP(String base) {
		return base + "/bestias/bestias.jsp";
	}
	
	public static String LISTAR_BESTIAS(String base) {
		return base + "/bestia/listar";
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
		return base + "/habitat/listar";
	}

	// Noticias
	public static String NOTICIA_FORM_JSP(String base) {
		return base + "/noticias/noticiaForms.jsp";
	}

	// Categorias
	public static String CATEGORIA_FORM_JSP(String base) {
		return base + "/categorias/categoriaForms.jsp";
	}

	// Evidencias
	public static String EVIDENCIA_FORM_JSP(String base) {
		return base + "/evidencias/evidenciaForms.jsp";
	}

	public static String TIPO_EVIDENCIA_FORM_JSP(String base) {
		return base + "/evidencias/tipoEvidenciaForms.jsp";
	}

	// Investigadores
	public static String SOLICITUDES_INVESTIGADOR_JSP(String base) {
		return base + "/investigadores/solicitudesInvestigador.jsp";
	}
	
	public static String PRESENTAR_CANDIDATURA_JSP(String base) {
		return base + "/invesigadores/presentarCandidatura.jsp";
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
	
	
}