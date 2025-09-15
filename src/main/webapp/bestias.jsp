<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Bestia" %>
<%@ page import="entities.Habitat" %>
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
 <%-- <%
            LinkedList<Bestia> bestias = (LinkedList<Bestia>) request.getAttribute("bestias");
            if (bestias != null) {
                for (Bestia bestia : bestias) {
                    <article class="bestia">
                    	<h1><%= bestia.getNombre() %></h1>
                    	<aside>
                    		<h2>Habitats</h2>
                    		<ul>
                    		<% LinkedList<Habitat> habitats =  bestia.getHabitats(); %>
                    		<%  if(habitats != null){
                    			for (Habitat habitat: habitats) { 
                    		%>
                    			<li><%= habitat.getNombre() %></li>
                    			<% } 
                    		} 
                    		else{ %>
                    			<li>Sin habitats registradas</li> 
                    		<% } %>
                    		</ul>
                    	</aside>
                    </article>
        <%
                }
            }
        %> 
    --%>
        <% Bestia bestia = new Bestia(1, "JUAN PEREZ", "PELIGROSISIMO"); %>
        <article class="bestia">
                    	<h1><%= bestia.getNombre() %></h1>
                    	<aside>
                    		<h4>Peligrosidad</h4>
                    		<p><%= bestia.getPeligrosidad() %></p>
                    		<h4>Habitats</h4>
                    		<ul>
                    		<% LinkedList<Habitat> habitats =  bestia.getHabitats(); %>
                    		<%  if(!habitats.isEmpty()){
                    			for (Habitat habitat: habitats) { 
                    		%>
                    			<li><%= habitat.getNombre() %></li>
                    			<% } 
                    		} 
                    		else{ %>
                    			<li>Sin habitats registradas</li> 
                    		<% } %>
                    		</ul>
                    	</aside>
                    	<button>Examinar</button>
        </article>
        <article class="bestia">
                    	<h1><%= bestia.getNombre() %></h1>
                    	<aside>
                    		<h4>Peligrosidad</h4>
                    		<p><%= bestia.getPeligrosidad() %></p>
                    		<h4>Habitats</h4>
                    		<ul>
                    		<% LinkedList<Habitat> habitas =  bestia.getHabitats(); %>
                    		<%  if(!habitats.isEmpty()){
                    			for (Habitat habitat: habitas) { 
                    		%>
                    			<li><%= habitat.getNombre() %></li>
                    			<% } 
                    		} 
                    		else{ %>
                    			<li>Sin habitats registradas</li> 
                    		<% } %>
                    		</ul>
                    	</aside>
                    	<button>Examinar</button>
        </article>
</section>
</body>
</html>