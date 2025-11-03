<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="entities.Registro" %>
<%@ page import="entities.Bestia" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registros pendientes</title>
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/navbar.css">
<link rel="stylesheet" href="css/registros.css">
</head>
<body>
<%@ include file="components/navbar.jsp" %>

<section class="mainContent">
    <h1>Registros pendientes</h1>

    <%
        Bestia bestia = (Bestia) request.getAttribute("bestia");
        LinkedList<Registro> registros = (LinkedList<Registro>) request.getAttribute("registrosPendientes");
    %>

    <% if (bestia != null) { %>
        <h2>Bestia: <%= bestia.getNombre() %></h2>
    <% } %>

    <% if (registros == null || registros.isEmpty()) { %>
        <div class="notFound">No hay registros pendientes para esta bestia.</div>
    <% } else { %>
        <table class="tablaRegistros">
            <thead>
                <tr>
                    <th>N° Registro</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
            <% for (Registro reg : registros) { %>
                <tr>
                    <td><%= reg.getNroRegistro() %></td>
                    <td class="acciones">
                        <a href="SvBestia?action=registro&id=<%= bestia.getIdBestia() %>&nroRegistro=<%=reg.getNroRegistro()%>">
                        	<button class="btnExaminar">Examinar</button>
                        </a>
                    	
                    	<form action="SvRegistro" method="post" style="display:flex;">
        					<input type="hidden" name="action" value="aceptar">
					        <input type="hidden" name="nroRegistro" value="<%= reg.getNroRegistro() %>">
					        <input type="hidden" name="idInvestigador" value="<%= usuario.getIdUsuario() %>">
					        <input type="hidden" name="idBestia" value="<%= reg.getBestia().getIdBestia() %>"/>
					        <button class="btnAceptar" type="submit">Aceptar</button>
    					</form>

					    <form action="SvRegistro" method="post" style="display:flex;">
					        <input type="hidden" name="action" value="rechazar">
					        <input type="hidden" name="nroRegistro" value="<%= reg.getNroRegistro() %>">
					        <input type="hidden" name="idBestia" value="<%= reg.getBestia().getIdBestia() %>"/>
					        <button class="btnRechazar" type="submit">Rechazar</button>
					    </form>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } %>
</section>
<footer></footer>
</body>
</html>
