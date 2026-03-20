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
        <link rel="stylesheet" href="../css/main.css">
        <link rel="stylesheet" href="../css/noticias.css">
        <link rel="stylesheet" href="../css/navbar.css">
    </head>
    <body>
        <%@ include file="components/navbar.jsp" %>
        <section class="mainContent">
			<h2>NOTICIAS</h2>
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
        <footer>
        	<p>© 2026 Bestiario S.A. Todos los derechos reservados.</p>
        </footer>
    </body>
</html>