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
			%>
			        <a href="logout.jsp" class="logInOut">Cerrar sesi�n</a>
			<%
			    } else {
			%>
					<a href="login.jsp" class="logInOut">Iniciar sesi�n</a>
			<%
			    }
			%>
</nav>