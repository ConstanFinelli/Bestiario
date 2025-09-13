<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ 
page import="entities.Usuario"
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Inicio</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="main.css">
        <link rel="stylesheet" href="home.css">
    </head>
    <body>
        <nav>
            <a>Inicio</a>
            <a>Bestias</a>
            <input type="text" placeholder="Buscar">
	        <%
			    Usuario usuario = (Usuario) session.getAttribute("user");
			    if (usuario != null) {
			%>
			        <a href="logout.jsp">Cerrar sesión</a>
			<%
			    } else {
			%>
					<a href="login.jsp">Iniciar sesión</a>
			<%
			    }
			%>
        </nav>
        <section class="mainContent">
            <section>
                <h2>Â¿QuiÃ©nes somos?</h2>
                <p>En (inserte nombre) nos dedicamos a recopilar, analizar y compartir informaciÃ³n sobre avistamientos de criaturas enigmÃ¡ticas alrededor del mundo. Desde leyendas ancestrales hasta testimonios recientes, nuestro objetivo es construir un registro accesible y confiable que acerque a la comunidad a estos fenÃ³menos misteriosos.</p>
            </section>
            <section>
                <h2>Â¿Que encontrarÃ¡s en este sitio?</h2>
                <p>AquÃ­ encontrarÃ¡s reportes de avistamientos, descripciones detalladas, mapas interactivos y artÃ­culos de investigaciÃ³n que buscan dar contexto cultural, histÃ³rico y cientÃ­fico a cada caso. Creemos que cada relato, ya sea una historia transmitida por generaciones o una experiencia vivida en primera persona, aporta una pieza valiosa al gran rompecabezas de lo desconocido.</p>
            </section>
        </section>
        <footer>
        </footer>
    </body>
</html>