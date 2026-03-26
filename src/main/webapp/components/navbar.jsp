<%@ 
page import="entities.Usuario, helpers.HttpRoutes"
%>
<head>
 <link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
</head>
<nav>
			<a class="navTitle" href=<%=HttpRoutes.HOME_JSP(request.getContextPath()) %> >Bestiario</a>
            <a class="navLink" href=<%=HttpRoutes.LISTAR_BESTIAS(request.getContextPath())%>>Bestias</a>
            <a class="navLink" href=<%=HttpRoutes.LISTAR_HABITATS(request.getContextPath())%>>Habitats</a>
            <a class="navLink" href=<%=HttpRoutes.LISTAR_NOTICIAS(request.getContextPath())%>>Noticias</a>
            
	        <%
			    Usuario usuario = (Usuario) session.getAttribute("user");
			    if (usuario != null){%>
			    	<%
			    	if(usuario.getEstado().equals("investigador")){
			    	%>
			    	<a class="navLink" href="<%= HttpRoutes.BESTIA_FORMS_JSP(request.getContextPath()) %>">Admin</a>
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
			%>
					<a href=<%=HttpRoutes.LOGIN_JSP(request.getContextPath()) %> class="logInOut">Iniciar sesión</a>
			<%
			    }
			%>
</nav>