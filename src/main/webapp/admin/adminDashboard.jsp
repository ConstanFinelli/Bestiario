<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<%
 String feedbackMessage = (String) request.getAttribute("feedbackMessage");
	Usuario user = (Usuario) session.getAttribute("user");
	if(user == null){
		response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
	}else if(!"investigador".equals(user.getEstado())){
		response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
	}
%>
<html>
<head>
	<meta charset="UTF-8">
	<title>Bestiario - Admin Dashboard</title>
	<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath())%>">
	<link rel="stylesheet" href="<%= HttpRoutes.ADMINDASHBOARD_CSS(request.getContextPath())%>">
    <link rel="stylesheet" href=<%= HttpRoutes.NAVBAR_CSS(request.getContextPath())%>>
</head>
<script>
		function changeFormTo(to, forms){
			for(const form of forms){
				if(form === to){
					const hiddenForms = document.getElementsByClassName(to);
					Array.from(hiddenForms).forEach((hiddenForm) =>{
						hiddenForm.classList.remove('hiddenForm')
					})
					document.getElementById(form + 'Btn').classList.add('activeButton');
				}else{
					const shownForms = document.getElementsByClassName(form);
					Array.from(shownForms).forEach((shownForm) =>{
						shownForm.classList.add('hiddenForm')
					})
					document.getElementById(form + 'Btn').classList.remove('activeButton');
				}
			}
		}
</script>
<body>
	<%@ include file="../../components/navbar.jsp" %>
	<div class="adminLayout">
		<aside class="dashboardMenu">
			<h2>Menu de Administrador</h2>
			<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>?crud=categorias" class="dashboardLink" id="catLink">Categorías</a>
			<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>?crud=habitats" class="dashboardLink" id="habLink">Habitats</a>
			<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>?crud=usuarios" class="dashboardLink" id="usLink">Usuarios</a>
			<a href="<%= HttpRoutes.ADMIN_DASHBOARD_JSP(request.getContextPath()) %>?crud=tiposEvidencia" class="dashboardLink" id="teLink">Tipos de evidencia</a>
		</aside>
		<main class="adminContent">
			<% if(feedbackMessage != null){ %>
				<span class="feedbackMessage"><%= feedbackMessage %></span>
			<%} %>
			<% String crud = request.getParameter("crud"); %>
			<% if(crud == null){%>
				<h2>Bienvenido al menú de Admin</h2>
				<p>Seleccione una opción para gestionar en el menú de la izquierda.</p>
			<%} else if ("categorias".equals(crud)){ %>
				<%@ include file="adminCategorias.jsp" %>
				<script type="text/javascript">document.getElementById("catLink").classList.add("activeLink")</script>
			<%} else if ("usuarios".equals(crud)){ %>
				<%@ include file="adminUsuarios.jsp" %>
				<script type="text/javascript">document.getElementById("usLink").classList.add("activeLink")</script>
			<%} else if ("habitats".equals(crud)){ %>
				<%@ include file="adminHabitats.jsp" %>
				<script type="text/javascript">document.getElementById("habLink").classList.add("activeLink")</script>
			<%} else if ("carHabitat".equals(crud)){%>
				<%@ include file="adminCarHabitats.jsp" %>
				<script type="text/javascript">document.getElementById("habLink").classList.add("activeLink")</script>
			<%} else if ("tiposEvidencia".equals(crud)){%>
				<%@ include file="adminTipoEvidencia.jsp" %>
				<script type="text/javascript">document.getElementById("teLink").classList.add("activeLink")</script>
			<%} %>
		</main>
	</div>
	<footer>
    	<p>© 2026 Bestiario S.A. Todos los derechos reservados.</p>
	</footer>
</body>
</html>