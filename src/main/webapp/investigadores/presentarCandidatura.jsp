<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="helpers.HttpRoutes"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registrar Candidatura a Investigador</title>
<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.BESTIAS_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.NUEVABESTIA_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath()) %>">
</head>
<body>
<%@ include file="../components/navbar.jsp" %>
<%
	String errorMsg = (String) request.getAttribute("errorMsg");
%>
<section class="mainContent">
	<% if(usuario != null){ %>
    <h1>Registrar Candidatura a Investigador</h1>
    
    <form action="<%=HttpRoutes.CREAR_SOLICITUD(request.getContextPath())%>?idUsuario=<%= usuario.getIdUsuario() %>" method="post" class="formBestia">
        <label for="nombre"><strong>Nombre:</strong></label><br>
        <input type="text" id="nombre" name="nombre" class="inputText" placeholder="Ej: Juan" required><br><br>

        <label for="apellido"><strong>Apellido:</strong></label><br>
        <input type="text" id="apellido" name="apellido" class="inputText" placeholder="Ej: Pérez" required><br><br>

        <label for="dni"><strong>DNI:</strong></label><br>
        <input type="text" id="dni" name="dni" class="inputText" placeholder="Ej: 12345678" required><br><br>

        <a href="<%= HttpRoutes.HOME_JSP(request.getContextPath()) %>" class="btnBestia">Cancelar</a>
        <button type="submit" class="btnBestia">Guardar →</button>
    </form>
    <%}else{ %>
    	<div class="notFound">Debes iniciar sesión para esta página.</div>
    <%} %>
    <% if(errorMsg != null){ %>
    	<div class="notFound">${errorMsg}</div>
    <%} %>
</section>
<%@ include file="../components/footer.jsp" %>
</body>
</html>
