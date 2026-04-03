<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entities.Noticia" %>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Bestiario - Redactar noticia</title>
        <meta charset="UTF-8">
        <link rel="preconnect" href="https://fonts.googleapis.com" />
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
		<link href="https://fonts.googleapis.com/css2?family=Bad+Script&family=MedievalSharp&display=swap" rel="stylesheet" />
        <link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
		<link rel="stylesheet" href="<%= HttpRoutes.NOTICIAS_CSS(request.getContextPath()) %>">
		<link rel="stylesheet" href="<%= HttpRoutes.REDACTARNOTICIA_CSS(request.getContextPath())%>">
		<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath()) %>">
    </head>
    <body>
        <%@ include file="../components/navbar.jsp" %>
	<section class="mainContent">
		<h2>REDACTAR NOTICIA</h2>
		<div class="formContainer">
			<form action="<%=HttpRoutes.CREAR_NOTICIA(request.getContextPath())%>" method="post" class="formNoticia">
				<label for="titulo" class="inputLabel"><strong>Título</strong></label><br>
				<input type="text" id="titulo" name="titulo" class="inputTitulo" placeholder="Ej: ¡Nuevo avistamiento!" required><br><br>
				<label for="titulo" class="inputLabel"><strong>Contenido</strong></label><br>
				<textarea id="contenido" name="contenido" class="inputContenido" placeholder="Insertar contenido de la noticia a comentar a los lectores." required></textarea><br><br>
				<div>
					<button onclick="location.href='<%=HttpRoutes.LISTAR_NOTICIAS(request.getContextPath())%>'" class="submitButton">Cancelar</button>
					<button type="submit" class="submitButton">Continuar →</button>
				</div>
			</form>
		</div>
	</section>
	<%@ include file="../components/footer.jsp" %>
    </body>
</html>