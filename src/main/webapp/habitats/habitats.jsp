<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="helpers.HttpRoutes" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Habitats</title>
<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.HABITATS_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath()) %>">
</head>
<body>
<%@ include file="../components/navbar.jsp" %>
<section class="mainContent" style="height:fit-content">
<h1>Habitats registradas</h1>
	<section class="habitats">
	<%
            LinkedList<Habitat> habitats = (LinkedList<Habitat>) request.getAttribute("habitats");
            if (habitats != null) {
                for (Habitat habitat : habitats) { %>
                    <article class="habitat">
                    	<h1><%= habitat.getNombre() %></h1>
                    	<aside>
                    		<h4>Localización</h4>
                    		<p><%= habitat.getLocalizacion() %></p>
                    		<h4>Caracteristicas</h4>
                    		<p><%= habitat.getCaracteristicas() %></p>
                    		<h4>Bestias vistas</h4>
                    		<ul>
                    			<% if(!habitat.getBestias().isEmpty()){
                    				for(Bestia bestia: habitat.getBestias()){
                    			%>
                    			<li>
                    				<a href="<%= HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) %>?id=<%= bestia.getIdBestia() %>"><%= bestia.getNombre() %></a>
                    			</li>
                    			<%}}else{ %>
                    				<li>No hay bestias registradas.</li>
                    			<%} %>
                    		</ul>
                    	</aside>
                    </article>
        <%
                }
            }else{
                %> 
                <section>
                	<div class="notFound">No se pudieron encontrar Habitats.</div>
                </section>
                <%} %>
        </section>
</section>
<footer>
</footer>
</body>
</html>