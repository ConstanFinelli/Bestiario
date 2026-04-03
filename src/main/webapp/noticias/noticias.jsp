<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entities.Noticia" %>
<%@ page import="java.util.LinkedList" %>
<!DOCTYPE html>
<% 
	LinkedList<Noticia> noticias = (LinkedList<Noticia>) request.getAttribute("noticias");
%>
<html>
    <head>
        <title>Bestiario - Noticias</title>
        <meta charset="UTF-8">
        <link rel="preconnect" href="https://fonts.googleapis.com" />
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
		<link href="https://fonts.googleapis.com/css2?family=Bad+Script&family=MedievalSharp&display=swap" rel="stylesheet" />
        <link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
		<link rel="stylesheet" href="<%= HttpRoutes.NOTICIAS_CSS(request.getContextPath()) %>">
		<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath()) %>">
    </head>
    <body>
        <%@ include file="../components/navbar.jsp" %>
        <section class="mainContent">
			<h2>NOTICIAS</h2>
		<%
		if (usuario != null) {
			if (usuario.getEstado().equals("investigador")) {
		%>
		<a class="btnAgregar"
			href="<%=HttpRoutes.REDACTARNOTICIA_JSP(request.getContextPath())%>">
			+ Redactar nueva noticia</a>
		<%
		}
		}
		%>
		<div class="newsContainer">
				<%if(noticias != null){ %>
					<% for(Noticia noticia : noticias){%>
							<article class="new">
								<div class="newContainer">
									<h2><%= noticia.getTitulo() %></h2>
									<span class="newInfo">escrito por 
											<%=noticia.getPublicador().getNombre() %> <%=noticia.getPublicador().getApellido() %> 
											- publicado <%= noticia.getFechaPublicacion() %></span>
									<p class="newContent">
										<%= noticia.getContenido() %>
									</p>
								</div>
							</article>
					<%} %>
				<%} %>
				</div>
		</section>
        <%@ include file="../components/footer.jsp" %>
    </body>
</html>