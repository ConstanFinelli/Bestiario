<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Registro" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Registro de bestia</title>
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/registro.css">
        <link rel="stylesheet" href="css/navbar.css">
    </head>
    <body>
        <%@ include file="components/navbar.jsp" %>
        <% 
        	Bestia bestia = (Bestia) request.getAttribute("bestia");
    		Registro ultimoRegistro = (Registro) request.getAttribute("ultimoRegistro");    
        %>
        <section class="mainContent">
        <% if(bestia == null){ %>
        <section>
        	<div class="notFound">Bestia no encontrada</div>
        </section>
        <%} %>
        <% if(bestia != null){ %>
            <section>
                <h1><%= bestia.getNombre() %>></h1>
                <% if(ultimoRegistro != null){ %>
                <h2>Introducción</h2>
                <p><%= ultimoRegistro.getContenido().getIntroduccion() %></p>
                <h2>Descripción</h2>
                <p><%= ultimoRegistro.getContenido().getDescripcion() %></p>
                <h2>Historia</h2>
                <p><%= ultimoRegistro.getContenido().getHistoria() %></p>
                <h2>Resumen</h2>
                <p><%= ultimoRegistro.getContenido().getResumen() %></p>
                <% }else{ %>
                <div class="notFound">No hay un registro encontrado para esta bestia.</div>
                <% } %>
            </section>
            <aside class="infoBestia">
                <img src="https://www.lanacion.com.ar/resizer/v2/las-fotos-encontradas-en-una-camara-escondida-en-OFV4RMZXVVHKHIHKGVMR2QETTA.jpg?auth=8e27602b93bb370ef1f97fb6135900dbccef34b4a4fa6e5693bd3335fe8f64a4&width=420&height=280&quality=70&smart=true">
                <div>
	                <h3>Detalles de la bestia</h3>
	                <ul>
	                    <li>Nombre: <%= bestia.getNombre() %> </li>
	                    <li>Peligrosidad: <%= bestia.getPeligrosidad() %></li>
	                </ul>
                </div>
                <div>
	                <h3>Hábitats localizados</h3>
	                <ul>
	                    <li>nombre, localización</li>
	                </ul>
                </div>
            </aside>
            <% } %>
        </section>
        <footer>
        </footer>
    </body>
</html>