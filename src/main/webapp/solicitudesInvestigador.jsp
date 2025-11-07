<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Investigador" %>
<%@ page import="java.time.LocalDate" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Candidaturas a Investigador</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/bestias.css">
<link rel="stylesheet" href="css/navbar.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>	
<section class="mainContent">
<h1>Candidaturas</h1>
	
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
            LinkedList<Investigador> candidaturas = (LinkedList<Investigador>) request.getAttribute("solicitantes");
            if (candidaturas != null) {
                for (Investigador candidato : candidaturas) {%>
                    <article class="bestia">
                    	<h1><%= candidato.getNombre() + candidato.getApellido()%></h1>
                    	<aside>
                    		<h4>Dni: </h4>
                    		<p>
           					<%= candidato.getDni()%>
                    		</p>
                    	</aside>
                    	<form action="SvUsuario" method="post" style="display:inline;">
    					<input type="hidden" name="action" value="aprobarSolicitud">
    					<input type="hidden" name="idUsuario" value="<%= candidato.getIdUsuario() %>">
    					<button type="submit" class="btnBestia">Aprobar</button>
						</form>
                    	<form action="SvUsuario" method="post" style="display:inline;">
					    <input type="hidden" name="action" value="rechazarSolicitud">
					    <input type="hidden" name="idUsuario" value="<%= candidato.getIdUsuario() %>">
					    <button type="submit" class="btnBestia">Rechazar</button>
						</form>
                    </article>
        <%
                } 
            }
                else{
        %> 
        <section>
        	<div class="notFound">No se pudieron encontrar Candidaturas.</div>
        </section>
        <%} %>
        
        </section>
</section>
<footer>
</footer>
</body>
</html>