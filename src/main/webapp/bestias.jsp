<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
<%@ page import="entities.Categoria" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Bestias</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bestias.css">
<link rel="stylesheet" href="css/navbar.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>
<section class="mainContent">
<h1>Bestias registradas</h1>
	<%
    	String errorMsg = (String) session.getAttribute("errorMsg");
    	if (errorMsg != null) {
	%>
    <div class="notFound"><%= errorMsg %></div>
	<%
        session.removeAttribute("errorMsg"); // para que no se repita
    	}
	%>
	
	<section class="bestias">
	<%
            LinkedList<Bestia> bestias = (LinkedList<Bestia>) request.getAttribute("bestias");
            if (bestias != null) {
                for (Bestia bestia : bestias) { %>
                    <article class="bestia">
                    	<h1><%= bestia.getNombre() %></h1>
                    	<aside>
                    		<h4>Peligrosidad</h4>
                    		<p><%= bestia.getPeligrosidad() %></p>
                    		<h4>Categorías</h4>
                    		<p>
                    		<% if(bestia.getCategorias().isEmpty()){ %>
                    			No tiene categorías definidas.
                    			<%}else{ 
                    				for(Categoria cat:bestia.getCategorias()){
                    			%>
                    				<%-- implementar lista de bestias de una categoria --%>
                    				<a href="SvCategoria?action=bestias"><%= cat.getNombre() %></a>
                    			<%}} %>
                    		</p>
                    	</aside>
                    	<a class="btnBestia" href="SvBestia?action=registro&id=<%= bestia.getIdBestia() %>">Examinar</a>
                    </article>
        <%
                }
            }else{
        %> 
        <section>
        	<div class="notFound">No se pudieron encontrar Bestias.</div>
        </section>
        <%} %>
        
        
        </section>
</section>
<footer>
</footer>
</body>
</html>