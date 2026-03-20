<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entities.Noticia, helpers.HttpRoutes" %>
<%@ page import="java.util.LinkedList" %>
<!DOCTYPE html>
<%
	LinkedList<Noticia> noticias = (LinkedList<Noticia>) request.getAttribute("noticias");	
	if(noticias == null ){
		RequestDispatcher rd = request.getRequestDispatcher(HttpRoutes.LISTAR_NOTICIAS("") + "?flag=ultimasNoticias");
		rd.forward(request, response);
	}
	System.out.println("HE LLEGADO AQUI");
%>
<html>
    <head>
        <title>Bestiario - Inicio</title>
        <meta charset="UTF-8">
        <link rel="preconnect" href="https://fonts.googleapis.com" />
		<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
		<link href="https://fonts.googleapis.com/css2?family=Bad+Script&family=MedievalSharp&display=swap" rel="stylesheet" />
        <link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath())%>">
        <link rel="stylesheet" href=<%= HttpRoutes.HOME_CSS(request.getContextPath()) %>>
        <link rel="stylesheet" href=<%= HttpRoutes.NAVBAR_CSS(request.getContextPath())%>>
    </head>
    <body>
        <%@ include file="components/navbar.jsp" %>
        <section class="mainContent">
			<section class="aboutUs">
				<div class="aboutUsBg">
					<section>
						<h2>¿Quiénes somos?</h2>
						<p>
							En Bestiario nos dedicamos a recopilar, analizar y compartir información sobre avistamientos de criaturas enigmáticas
							alrededor del mundo. Desde leyendas ancestrales hasta testimonios recientes, nuestro objetivo es construir un registro
							accesible y confiable que acerque a la comunidad a estos fenómenos misteriosos.
						</p>
					</section>
					<section>
						<h2>¿Que encontrarás en este sitio?</h2>
						<p>
							Aquí­ encontrarás reportes de avistamientos, descripciones detalladas, mapas interactivos y artículos de investigación que
							buscan dar contexto cultural, histórico y científico a cada caso. Creemos que cada relato, ya sea una historia transmitida
							por generaciones o una experiencia vivida en primera persona, aporta una pieza valiosa al gran rompecabezas de lo
							desconocido.
						</p>
					</section>
				</div>
			</section>
			<aside class="newsContent">
				<h2>Últimas noticias</h2>
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
			</aside>
		</section>
        <footer>
        	<p>© 2026 Bestiario S.A. Todos los derechos reservados.</p>
        </footer>
    </body>
</html>