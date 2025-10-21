<%@ 
page import="entities.Usuario"
%>
<nav>
            <a class="navLink" href="home.jsp">Inicio</a>
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