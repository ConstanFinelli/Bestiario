<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Habitats</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/habitats.css">
<link rel="stylesheet" href="css/navbar.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>
<section class="mainContent">
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
                    			
                    			</li>
                    			<%}}else{ %>
                    				<li>No hay bestias registradas.</li>
                    			<%} %>
                    		</ul>
                    	</aside>
                    </article>
        <%
                }
            }
        %> 
        </section>
</section>
<footer>
</footer>
</body>
</html>