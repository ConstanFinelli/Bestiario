<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Investigador" %>
<%@ page import="helpers.HttpRoutes" %>

<!DOCTYPE html>
<%
	Usuario user = (Usuario) session.getAttribute("user");
	if(user == null){
		response.sendRedirect(HttpRoutes.HOME_JSP(request.getContextPath()));
		return;
	}
%>
<html>
<head>
<meta charset="UTF-8">
<title>Bestiario - Candidaturas a Investigador</title>
<link rel="stylesheet" href="<%= HttpRoutes.MAIN_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.BESTIAS_CSS(request.getContextPath()) %>">
<link rel="stylesheet" href="<%= HttpRoutes.NAVBAR_CSS(request.getContextPath()) %>">
</head>
<body>
<%@ include file="../components/navbar.jsp" %>	
<section class="mainContent">
<h1>Candidaturas</h1>
	
	<%
    	String errorMsg = (String) request.getAttribute("errorMsg");
    	if (errorMsg != null) {
	%>
    <div class="notFound"><%= errorMsg %></div>
	<%
    	}
	%>
	
	<section class="bestias">
	<%
            LinkedList<Investigador> candidaturas = (LinkedList<Investigador>) request.getAttribute("solicitantes");
            if (candidaturas != null && !candidaturas.isEmpty()) {
                for (Investigador candidato : candidaturas) {%>
                    <article class="candidatura">
                    	<h1><%= candidato.getNombre() + " " + candidato.getApellido()%></h1>
                    	<aside>
                    		<div class="dniCandidatura">DNI:<span><%= candidato.getDni()%></span> </div> 
                    		
                    	</aside>
                    	<div>
                    	<form action="<%=HttpRoutes.APROBAR_SOLICITUD(request.getContextPath()) %>" method="post" style="display:inline;">
    					<input type="hidden" name="idUsuario" value="<%= candidato.getIdUsuario() %>">
    					<button type="submit" class="btnAceptar">Aprobar</button>
						</form>
                    	<button type="button" class="btnRechazar" onclick="abrirModalForm('RECHAZAR SOLICITUD', '¿Estás seguro que deseas rechazar la solicitud de <%= (candidato.getNombre() + " " + candidato.getApellido()).replace("'", "\\'") %>?', '<%= HttpRoutes.RECHAZAR_SOLICITUD(request.getContextPath()) %>', 'idUsuario', '<%= candidato.getIdUsuario() %>', 'Rechazar')">Rechazar</button>
						</div>
                    </article>
        <%
                } 
            }
                else{
        %> 
        <section>
        	<div class="notFound">No hay candidaturas en este momento.</div>
        </section>
        <%} %>
        
        </section>
</section>
<%@ include file="../components/footer.jsp" %>
<%@ include file="../components/modalConfirmacion.jsp" %>
</body>
</html>