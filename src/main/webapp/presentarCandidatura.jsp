<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registrar Candidatura a Investigador</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bestias.css">
<link rel="stylesheet" href="css/navbar.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>
<section class="mainContent">
    <h1>Registrar Candidatura a Investigador</h1>
    
    <form action="SvUsuario?action=crearSolicitud&idUsuario=<%= usuario.getIdUsuario() %>" method="post" class="formBestia">
        <label for="nombre"><strong>Nombre:</strong></label><br>
        <input type="text" id="nombre" name="nombre" class="inputText" placeholder="Ej: Juan" required><br><br>

        <label for="apellido"><strong>Apellido:</strong></label><br>
        <input type="text" id="apellido" name="apellido" class="inputText" placeholder="Ej: Pérez" required><br><br>

        <label for="dni"><strong>DNI:</strong></label><br>
        <input type="text" id="dni" name="dni" class="inputText" placeholder="Ej: 12345678" required><br><br>

        <a href="home.jsp" class="btnBestia">Cancelar</a>
        <button type="submit" class="btnBestia">Guardar →</button>
    </form>
</section>

<footer></footer>
</body>
</html>
