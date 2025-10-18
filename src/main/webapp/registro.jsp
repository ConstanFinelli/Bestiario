<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Registro" %>
<%@ page import="entities.Evidencia" %>
<%@ page import="entities.Categoria" %>
<%@ page import="entities.Comentario" %>
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
    		LinkedList<Evidencia> evidencias = bestia.getEvidencias();
    		LinkedList<Comentario> comentarios = bestia.getComentarios();
        %>
        <section class="mainContent">
        <% if(bestia == null){ %>
        <section>
        	<div class="notFound">Bestia no encontrada</div>
        </section>
        <%} %>
        <% if(bestia != null){ %>
            <section>
                <h1><%= bestia.getNombre() %></h1>
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
                <% if(!evidencias.isEmpty()){ %>
                <h2>Evidencias</h2>
                <ul class="evidencias">
                <% for(Evidencia evidencia : evidencias){ %>
                	<%
                	String teDesc = evidencia.getTipo().getDescripcion();
                	String fechaOb = evidencia.getFechaObtencion().toString();
                	String evText = teDesc + ", obtenido el " + fechaOb;
                	
                	%>
                	<li class="evidencias-item">
                		<a href="<%= evidencia.getLink() %>"><%= evText %></a>
                	</li>
                <%} %>
                </ul>
                <%} %>
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
                	<h3>Categorías</h3>
                	<ul>
                	<% if(bestia.getCategorias().isEmpty()){ %>
                    			No tiene categorías definidas.
                    			<%}else{ 
                    				for(Categoria cat:bestia.getCategorias()){
                    			%>
                    				<li>
                    				<a href="SvCategoria?action=bestias"><%= cat.getNombre() %></a>
                    				</li>
                    			<%}} %>
                	</ul>
                </div>
                <div>
	                <h3>Hábitats localizados</h3>
	                <ul>
	                	<% LinkedList<Habitat> habitats = bestia.getHabitats(); %>
	                	<% if(habitats != null){ 
	                	for(Habitat habitat:habitats){
	                	%>
	                    <li><%= habitat.getNombre() %>, <%= habitat.getLocalizacion() %></li>
	                    <%} %>
	                    <% } else{%>
	                    <li>No hay habitats registradas para esta bestia.</li>
	                    <%} %>
	                </ul>
                </div>
                <div>
                	<h3>Detalles de registro</h3>
                	<ul>
                		<li>Publicado por <%= ultimoRegistro.getPublicador().getNombre() + " " + ultimoRegistro.getPublicador().getApellido() %></li>
                		<li>Último cambio: <%= ultimoRegistro.getFechaAprobacion() %></li>
                	</ul>
                </div>
            </aside>
            <% } %>
        </section>
        <section class="comentarios mainContent">
        		<h2>Comentarios</h2>
            	<% if(usuario != null){ %>
            	<form action="${pageContext.request.requestURL}" method="post">
	            	<input type="text" placeholder="Escribir comentario..." name="contenido">
	            	<input type="hidden" name="idUsuario" value="<%=usuario.getIdUsuario()%>">
	            	<input type="hidden" name="idBestia" value="<%=bestia.getIdBestia()%>">
	            	<input class="btnComentario" type="submit">
            	</form>
            	<%} 
            	if(!comentarios.isEmpty()){
            	for(Comentario comentario:comentarios){
            	%>
            	<section class="comentario">
            		<aside class="comentarioDatos"><%= comentario.getPublicador().getCorreo() %> - <span class="fechaComentario">publicado el <%= comentario.getFecha() %></span></aside>
            		<article class="comentarioContenido"><%= comentario.getContenido() %></article>
            	</section>
            	<%}}else{%>
            	<p>No hay comentarios para esta bestia.</p>
            	<%} %>
            </section>
        <footer>
        </footer>
    </body>
</html>