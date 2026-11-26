<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Bestiario - Admin Dashboard</title>
	<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath())%>">
	<link rel="stylesheet" href="<%= HttpRoutes.ADMINDASHBOARD_CSS(request.getContextPath())%>">
    <link rel="stylesheet" href=<%= HttpRoutes.NAVBAR_CSS(request.getContextPath())%>>
</head>
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
	<%@ include file="../components/footer.jsp" %>
	<%@ include file="../components/modalConfirmacion.jsp" %>
</body>
</html>