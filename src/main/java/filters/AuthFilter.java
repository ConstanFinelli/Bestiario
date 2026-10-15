package filters;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.Usuario;
import helpers.HttpRoutes;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
            ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".svg", ".ico", ".woff", ".woff2", ".ttf");

    private static final List<String> PUBLIC_PREFIXES = Arrays.asList(
            "/css/", "/js/", "/img/", "/images/", "/public/", "/auth/");

    private static final Set<String> PUBLIC_EXACT_PATHS = new HashSet<>(Arrays.asList(
            "", "/", "/home.jsp", "/forgot-password",
            "/bestias/listar", "/bestias/bestias.jsp",
            "/registros/obtenerRegistroConBestia", "/registros/registro.jsp",
            "/noticias/listar", "/noticias/noticias.jsp",
            "/mapas/mapa.jsp", "/mapas/bestia", "/mapas/bestias"));

    private static final List<String> INVESTIGADOR_PREFIXES = Arrays.asList(
            "/admin/", "/lectores/", "/habitats/", "/categorias/");

    private static final Set<String> INVESTIGADOR_EXACT_PATHS = new HashSet<>(Arrays.asList(
            "/investigadores/listarSolicitantes",
            "/investigadores/solicitudesInvestigador.jsp",
            "/investigadores/aprobarSolicitud",
            "/investigadores/rechazarSolicitud",
            "/bestias/aprobar",
            "/bestias/eliminar",
            "/bestias/editar",
            "/bestias/editarBestia.jsp",
            "/bestias/actualizar",
            "/bestias/cambiarHabitat",
            "/bestias/cambiarCategoria",
            "/registros/aceptarRegistro",
            "/registros/registrosPendientes.jsp",
            "/registros/obtenerRegistrosPendientesBestia",
            "/evidencias/aprobar",
            "/evidencias/eliminar",
            "/evidencias/crearTipoEvidencia",
            "/evidencias/actualizarTipoEvidencia",
            "/evidencias/eliminarTipoEvidencia",
            "/evidencias/listarTiposEvidencia",
            "/noticias/crear",
            "/noticias/eliminar",
            "/noticias/redactarNoticia.jsp",
            "/comentarios/eliminar",
            "/mapas/mapaCargaHabitat.jsp"));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());

        for (String ext : STATIC_EXTENSIONS) {
            if (path.toLowerCase().endsWith(ext)) {
                chain.doFilter(request, response);
                return;
            }
        }

        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) {
                chain.doFilter(request, response);
                return;
            }
        }

        if (PUBLIC_EXACT_PATHS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("user") : null;

        if (usuario == null) {
            if (session == null) {
                session = httpRequest.getSession(true);
            }
            session.setAttribute("logMsg", "Debe iniciar sesión para acceder a la funcionalidad solicitada.");

            String redirectUrl = HttpRoutes.LOGIN_JSP(contextPath);
            if ("GET".equalsIgnoreCase(httpRequest.getMethod())) {
                String query = httpRequest.getQueryString();
                String fullUrl = uri + (query != null && !query.isEmpty() ? "?" + query : "");
                redirectUrl += "?urlAnterior=" + URLEncoder.encode(fullUrl, StandardCharsets.UTF_8);
            }
            httpResponse.sendRedirect(redirectUrl);
            return;
        }

        boolean requiresInvestigador = isInvestigadorPath(path);
        if (requiresInvestigador && !"investigador".equalsIgnoreCase(usuario.getEstado())) {
            session.setAttribute("errorGlobal", "Acceso denegado: se requieren permisos de investigador.");
            httpResponse.sendRedirect(HttpRoutes.HOME_JSP(contextPath));
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isInvestigadorPath(String path) {
        if (INVESTIGADOR_EXACT_PATHS.contains(path)) {
            return true;
        }
        for (String prefix : INVESTIGADOR_PREFIXES) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
