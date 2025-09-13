<%@ 
page import="entities.Usuario"
%>
<nav>
            <a href="home.jsp">Inicio</a>
            <a>Bestias</a>
            <a href="registro.jsp">Registro (temp)</a>
            <input type="text" placeholder="Buscar">
	        <%
			    Usuario usuario = (Usuario) session.getAttribute("user");
			    if (usuario != null) {
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