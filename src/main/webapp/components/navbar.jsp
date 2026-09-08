<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entities.Usuario, helpers.HttpRoutes" %>
<%@ include file="error.jsp" %>
<head>
 <link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
</head>
<nav>
			<a class="navTitle" href=<%=HttpRoutes.HOME_JSP(request.getContextPath()) %> >Bestiario</a>
            <a class="navLink" href=<%=HttpRoutes.LISTAR_BESTIAS(request.getContextPath())%>>Bestias</a>
            <a class="navLink" href=<%=HttpRoutes.MAPA_BESTIAS(request.getContextPath())%>>Mapa</a>
            <a class="navLink" href=<%=HttpRoutes.LISTAR_NOTICIAS(request.getContextPath())%>>Noticias</a>
            
	        <%
			    Usuario usuario = (Usuario) session.getAttribute("user");
			    if (usuario != null){%>
			    	<%
			    	if(usuario.getEstado().equals("investigador")){
			    	%>
			    	<a class="navLink" href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>">Admin</a>
			    	<a class="navLink" href=<%=HttpRoutes.LISTAR_SOLICITANTES(request.getContextPath()) %>>Candidaturas</a>
			    	<%
			    	} else if(usuario.getEstado().equals("lector")){ %>
			    		<a class="navLink" href=<%=HttpRoutes.PRESENTAR_CANDIDATURA_JSP(request.getContextPath()) %> >Volverse Investigador</a>    	
			<% } else if(usuario.getEstado().equals("solicitante")){%>
						<span class="navLink"> Ya ha enviado una solicitud</span>
			<% }%>
			        <a href=<%=HttpRoutes.LOGOUT_JSP(request.getContextPath()) %>  class="logInOut">Cerrar sesión</a>
			<%
			    } else {
				String forwardURI = (String) request.getAttribute("jakarta.servlet.forward.request_uri");
				String forwardQuery = (String) request.getAttribute("jakarta.servlet.forward.query_string");
				String uri = (forwardURI != null) ? forwardURI : request.getRequestURI();
				String query = (forwardURI != null) ? forwardQuery : request.getQueryString();
				String urlActual = uri + (query != null && !query.isEmpty() ? "?" + query : "");
			%>
					<form action="<%= HttpRoutes.LOGIN_JSP(request.getContextPath())%>">
					<input type="hidden" name="urlAnterior" value="<%=urlActual%>">
					<button	type="submit" class="logInOut">Iniciar Sesion</button>
					</form>
			<%
			    }
			%>
</nav>
