<%@ 
page import="entities.Usuario"
%>
<head>
 <link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
</head>
<nav>
			<a class="navTitle" href="home.jsp">Bestiario</a>
            <a class="navLink" href="SvBestia?action=list">Bestias</a>
            <a class="navLink" href="SvHabitat?action=list">Habitats</a>
            
	        <%
			    Usuario usuario = (Usuario) session.getAttribute("user");
			    if (usuario != null) {
			    	if(usuario.isEsInvestigador()){
			    	%>
			    	<a class="navLink" href="SvBestia?action=form">Admin</a>
			    	<%
			    	}
			%>
			        <a href="logout.jsp" class="logInOut">Cerrar sesión</a>
			<%
			    } else {
			%>
					<a href="login.jsp" class="logInOut">Iniciar sesión</a>
			<%
			    }
			%>
</nav>