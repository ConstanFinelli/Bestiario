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
            
	        <%
			    Usuario usuario = (Usuario) session.getAttribute("user");
			    if (usuario != null) { %>
			    	<%
			    	if(usuario.getEstado().equals("investigador")){
			    	%>
			    	<a class="navLink" href="SvBestia?action=form">Admin</a>
			    	<a class="navLink" href=<%=HttpRoutes.LISTAR_SOLICITANTES(request.getContextPath()) %>>Candidaturas</a>
			    	<%
			    	} else{ %>
			    		<a class="navLink" href=<%=HttpRoutes.PRESENTAR_CANDIDATURA_JSP(request.getContextPath()) %> >Volverse Investigador</a>    	
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